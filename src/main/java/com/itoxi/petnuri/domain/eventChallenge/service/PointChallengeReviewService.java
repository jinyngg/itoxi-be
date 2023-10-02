package com.itoxi.petnuri.domain.eventChallenge.service;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.ALREADY_WRITTEN_REVIEW;

import com.itoxi.petnuri.domain.eventChallenge.dto.request.WritePointChallengeReviewRequest;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReview;
import com.itoxi.petnuri.domain.eventChallenge.repository.PointChallengeRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.entity.MainCategory;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PointChallengeReviewService {

    private final PointChallengeRepository pointChallengeRepository;
    private final PetTalkRepository petTalkRepository;

    public void write(
            Long pointChallengeId,
            MultipartFile file,
            WritePointChallengeReviewRequest request,
            PrincipalDetails principalDetails) {
        // 1. 로그인된 회원 조회
        Member reviewer = principalDetails.getMember();

        // 2. 챌린지 조회
        PointChallenge pointChallenge =
                pointChallengeRepository.getPointChallengeById(pointChallengeId);

        // 3. 당일에 작성된 리뷰가 존재하는지 확인
        if (pointChallengeRepository.isWritePointChallengeReviewToday(pointChallenge, reviewer)) {
            throw new Exception400(ALREADY_WRITTEN_REVIEW);
        }

        // 4. 리뷰 생성
        PointChallengeReview pointChallengeReview = PointChallengeReview.builder()
                .reviewer(reviewer)
                .pointChallenge(pointChallenge)
                .photoName(file.getName())
                .content(request.getContent())
                .build();

        // 5. 리뷰 사진 업로드
        pointChallengeRepository.uploadPointChallengeReviewPhoto(file, pointChallengeReview);
        
        // 6. 리뷰 저장
        pointChallengeRepository.writePointChallengeReview(pointChallengeReview);

        // 7. 펫톡 업데이트
        // 7-1. 자유수다 카테고리 조회
        MainCategory mainCategory = petTalkRepository.getMainCategoryById(2L);

        // 7-2. 펫톡 게시글 작성
        PetTalk petTalk =
                PetTalk.createByChallengeReview(
                        reviewer,
                        pointChallenge.getTitle(),
                        pointChallengeReview.getContent(),
                        mainCategory,
                        request.getPetType());

        // 7-3. 펫톡 게시글 저장
        petTalkRepository.write(petTalk);
        petTalkRepository.uploadPetTalkPhotos(new MultipartFile[]{file}, petTalk);
    }

    @Transactional(readOnly = true)
    public List<PointChallengeReview> loadMyReviewsOfThePointChallenge(
            Authentication authentication, Long pointChallengeId) {
        // 1. 로그인이 되어있지 않을 경우, 빈 리스트 리턴
        if (authentication == null) {
            return new ArrayList<>();
        }

        // 2. 로그인된 유저 조회
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member challenger = principalDetails.getMember();

        // 3. 챌린지 조회
        PointChallenge pointChallenge =
                pointChallengeRepository.getPointChallengeById(pointChallengeId);

        // 4. 로그인된 유저의 챌린지 리뷰 조회
        return pointChallengeRepository.loadMyReviewsOfThePointChallenge(
                challenger, pointChallenge);
    }

}
