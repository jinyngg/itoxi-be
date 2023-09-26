package com.itoxi.petnuri.domain.eventChallenge.service;

import com.itoxi.petnuri.domain.eventChallenge.dto.request.WriteReviewReq;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallengeReview;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenger;
import com.itoxi.petnuri.domain.eventChallenge.repository.RewardChallengeRepository;
import com.itoxi.petnuri.domain.eventChallenge.repository.RewardChallengeReviewRepository;
import com.itoxi.petnuri.domain.eventChallenge.repository.RewardChallengerRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.entity.MainCategory;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.common.exception.Exception404;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class RewardChallengeReviewService {
    private final RewardChallengeRepository challengeRepository;
    private final RewardChallengerRepository challengerRepository;
    private final RewardChallengeReviewRepository reviewRepository;
    private final PetTalkRepository petTalkRepository;
    private final AmazonS3Service amazonS3Service;

    @Transactional
    public void writeReview(Member member, Long challengeId, MultipartFile file, WriteReviewReq request) {
        // 챌린지
        RewardChallenge rewardChallenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new Exception404(NOT_FOUND_CHALLENGE_ID));

        // 챌린지 신청 여부 검사
        RewardChallenger rewardChallenger = challengerRepository.findByChallengerAndRewardChallenge(member, rewardChallenge)
                .orElseThrow(() -> new Exception404(NOT_FOUND_CHALLENGE_JOIN));

        // 리뷰 등록 여부 검사
        if (reviewRepository.existsByChallengerAndRewardChallengeId(member, challengeId)) {
            throw new Exception400(DUPE_POST_MEMBER);
        }

        // 리뷰 저장
        String savedUrl = amazonS3Service.uploadRewardChallengeReviewImage(file);
        RewardChallengeReview review = RewardChallengeReview.create(member, rewardChallenger.getRewardChallenge(),
                request.getContent(), file.getOriginalFilename(), savedUrl);
        reviewRepository.save(review);

        // 펫톡 저장
        MainCategory mainCategory = petTalkRepository.getMainCategoryById(2L);
        PetTalk petTalk = PetTalk.createByChallengeReview(member, rewardChallenge.getTitle(), review.getContent()
                , mainCategory, PetType.DOG); // @REVIEW PetType 기획 미정! 추후 수정 필요

        petTalkRepository.write(petTalk);
        petTalkRepository.uploadPetTalkPhotos(new MultipartFile[]{file}, petTalk);

        // 참여 프로세스 업데이트
        rewardChallenger.reviewComplete();
        challengerRepository.save(rewardChallenger);
    }
}
