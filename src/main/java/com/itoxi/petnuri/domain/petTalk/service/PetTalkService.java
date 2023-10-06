package com.itoxi.petnuri.domain.petTalk.service;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.MISMATCH_PET_TALK_WRITER;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkRequest;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkView;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import com.itoxi.petnuri.domain.petTalk.type.OrderType;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.redis.RedisService;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PetTalkService {

    private final PetTalkRepository petTalkRepository;
    private final RedisService redisService;

    @Transactional
    public void write(
            PrincipalDetails principalDetails, MultipartFile[] files, WritePetTalkRequest request) {
        Member member = principalDetails.getMember();

        // 2. 게시글 생성
        PetTalk petTalk = PetTalk.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .mainCategory(petTalkRepository.getMainCategoryById(request.getMainCategoryId()))
                .subCategory(request.getSubCategoryId() != null ?
                        petTalkRepository.getSubCategoryById(request.getSubCategoryId()) : null)
                .petType(request.getPetType())
                .writer(member)
                .build();

        // 3. 게시글 이미지 업로드
        petTalkRepository.uploadPetTalkPhotos(files, petTalk);

        // 4. 게시글 저장
        petTalkRepository.write(petTalk);
    }

    @Transactional(readOnly = true)
    public Page<PetTalkView> loadPetTalkPosts(
            Authentication authentication, Long mainCategoryId, Long subCategoryId,
            PetType petType, OrderType order, int page, int size) {
        // 1. 로그인된 회원 정보 확인
        Member member = null;
        if (authentication != null) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            member = principalDetails.getMember();
        }

        // 2. 정렬 방식에 따른 펫톡 게시글 조회
        boolean isNonMember = (member == null);
        switch (order) {
            case BEST:
                Page<PetTalkView> bestPetTalks = petTalkRepository.loadBestPetTalkViewsByCategoryAndPetType(
                        page, size, mainCategoryId, subCategoryId, petType);

                if (isNonMember) {
                    bestPetTalks.stream()
                            .filter(petTalkView -> petTalkView.getMemberId() != null)
                            .forEach(petTalkRepository::updateWriterAndPhotosToPetTalkView);
                    return bestPetTalks;
                }
                petTalkRepository.updateReactedStatusByMemberAndPetTalkViews(bestPetTalks, member);
                bestPetTalks.stream()
                        .filter(petTalkView -> petTalkView.getMemberId() != null)
                        .forEach(petTalkRepository::updateWriterAndPhotosToPetTalkView);
                return bestPetTalks;

            case LATEST:
            default:
                Page<PetTalkView> latestPetTalks = petTalkRepository.loadLatestPetTalkPostsByCategoryAndPetType(
                        page, size, mainCategoryId, subCategoryId, petType);

                if (isNonMember) {
                    latestPetTalks.stream()
                            .filter(petTalkView -> petTalkView.getMemberId() != null)
                            .forEach(petTalkRepository::updateWriterAndPhotosToPetTalkView);
                    return latestPetTalks;
                }

                petTalkRepository.updateReactedStatusByMemberAndPetTalkViews(latestPetTalks, member);
                latestPetTalks.stream()
                        .filter(petTalkView -> petTalkView.getMemberId() != null)
                        .forEach(petTalkRepository::updateWriterAndPhotosToPetTalkView);
                return latestPetTalks;
        }
    }

    @Transactional(readOnly = true)
    public PetTalkView loadPetTalkPostDetails(Authentication authentication, Long petTalkId) {
        // 1. 로그인된 회원 정보 확인
        Member member = null;
        if (authentication != null) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            member = principalDetails.getMember();
        }

        // 2. 게시글 조회
        PetTalkView petTalkView = petTalkRepository.loadPetTalkPostsDetails(petTalkId);

        // 3. 로그인된 사용자 이모지 여부 확인
        if (member != null) {
            petTalkRepository.updateReactedStatusByMemberAndPetTalk(petTalkView, member);
            petTalkRepository.reactEmoji(petTalkId, petTalkView);
        }

        // 4. 조회수 증가
        redisService.increasePetTalkViewCountToRedis(petTalkView);

        if (petTalkView.getMemberId() != null) {
            petTalkRepository.updateWriterAndPhotosToPetTalkView(petTalkView);
        }

        return petTalkView;
    }

    @Transactional
    public void deletePetTalkPost(PrincipalDetails principalDetails, Long PetTalkId) {
        Member member = principalDetails.getMember();
        PetTalk petTalk = petTalkRepository.getById(PetTalkId);

        if (!Objects.equals(member.getId(), petTalk.getWriter().getId())) {
            throw new Exception400(MISMATCH_PET_TALK_WRITER);
        }

        petTalkRepository.deletePetTalkPost(petTalk);
    }

}
