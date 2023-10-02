package com.itoxi.petnuri.domain.petTalk.service;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.MISMATCH_PET_TALK_WRITER;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkRequest;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
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
    public Page<PetTalk> loadPetTalkPosts(
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
                Page<PetTalk> bestPetTalks = petTalkRepository.loadBestPetTalkPostsByCategoryAndPetType(
                        page, size, mainCategoryId, subCategoryId, petType);
                if (isNonMember) {
                    return bestPetTalks;
                }
                petTalkRepository.updateReactedStatusByMemberAndPetTalks(bestPetTalks, member);
                return bestPetTalks;

            case LATEST:

            default:
                Page<PetTalk> latestPetTalks = petTalkRepository.loadLatestPetTalkPostsByCategoryAndPetType(
                        page, size, mainCategoryId, subCategoryId, petType);
                if (isNonMember) {
                    return latestPetTalks;
                }
                petTalkRepository.updateReactedStatusByMemberAndPetTalks(latestPetTalks, member);
                return latestPetTalks;
        }
    }

    @Transactional(readOnly = true)
    public PetTalk loadPetTalkPostDetails(Authentication authentication, Long petTalkId) {
        // 1. 로그인된 회원 정보 확인
        Member member = null;
        if (authentication != null) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            member = principalDetails.getMember();
        }

        // 2. 게시글 조회
        PetTalk petTalk = petTalkRepository.loadPetTalkPostsDetails(petTalkId);

        // 3. 로그인된 사용자 이모지 여부 확인
        if (member != null) {
            petTalkRepository.updateReactedStatusByMemberAndPetTalk(petTalk, member);
        }
        
        // 4. 조회수 증가
        redisService.increasePetTalkViewCountToRedis(petTalk);

        return petTalk;
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
