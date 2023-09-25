package com.itoxi.petnuri.domain.eventChallenge.service;

import com.itoxi.petnuri.domain.delivery.entity.ChallengeDelivery;
import com.itoxi.petnuri.domain.delivery.repository.ChallengeDeliveryRepository;
import com.itoxi.petnuri.domain.eventChallenge.dto.request.CreateChallengerReq;
import com.itoxi.petnuri.domain.eventChallenge.dto.response.GetMyRewardChallengeJoinResp;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenger;
import com.itoxi.petnuri.domain.eventChallenge.repository.RewardChallengeRepository;
import com.itoxi.petnuri.domain.eventChallenge.repository.RewardChallengerRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.product.entity.ChallengeProduct;
import com.itoxi.petnuri.domain.product.repository.ChallengeProductRepository;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.common.exception.Exception404;
import com.itoxi.petnuri.global.common.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.itoxi.petnuri.domain.product.type.ChallengeProductCategory.KIT;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class RewardChallengerService {
    private final RewardChallengeRepository challengeRepository;
    private final RewardChallengerRepository challengerRepository;
    private final ChallengeProductRepository challengeProductRepository;
    private final ChallengeDeliveryRepository challengeDeliveryRepository;

    @Transactional(readOnly = true)
    public GetMyRewardChallengeJoinResp getMyJoin(Member member, Long challengeId) {
        RewardChallenger rewardChallenger = challengerRepository.findByChallengerAndRewardChallengeId(member, challengeId)
                .orElseThrow(() -> new Exception404(NOT_FOUND_CHALLENGE_JOIN));

        return new GetMyRewardChallengeJoinResp(rewardChallenger.getProcess());
    }

    @Transactional
    public void create(Member member, Long challengeId, CreateChallengerReq request) {
        // 챌린지
        RewardChallenge rewardChallenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new Exception404(NOT_FOUND_CHALLENGE_ID));

        // 챌린지 신청 여부 검사. 이미 있으면 에러
        if (challengerRepository.existsByChallengerAndRewardChallenge(member, rewardChallenge)) {
            throw new Exception400(DUPE_CHALLENGE_JOIN);
        }

        // 챌린지 참여 저장
        RewardChallenger rewardChallenger = RewardChallenger.create(member, rewardChallenge, request.getIsConsentedPersonalInfo());
        challengerRepository.save(rewardChallenger);

        // 키트 배송 저장
        ChallengeProduct kit = challengeProductRepository.findByRewardChallengeAndCategory(rewardChallenge, KIT)
                .orElseThrow(() -> new Exception500(NOT_FOUND_CHALLENGE_KIT));
        ChallengeDelivery kitDelivery = ChallengeDelivery.create(member, kit, request.getDelivery());
        challengeDeliveryRepository.save(kitDelivery);

        // 리워드 배송 저장
        ChallengeProduct reward = challengeProductRepository.findById(request.getRewardId())
                .orElseThrow(() -> new Exception404(NOT_FOUND_CHALLENGE_PRODUCT_ID));
        ChallengeDelivery rewardDelivery = ChallengeDelivery.create(member, reward, request.getDelivery());
        challengeDeliveryRepository.save(rewardDelivery);
    }
}
