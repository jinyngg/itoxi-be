package com.itoxi.petnuri.domain.eventChallenge.service;

import com.itoxi.petnuri.domain.eventChallenge.dto.response.GetRewardChallengeDetailResp;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.eventChallenge.repository.RewardChallengeRepository;
import com.itoxi.petnuri.global.common.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_CHALLENGE_ID;

@Service
@RequiredArgsConstructor
public class RewardChallengeService {
    private final RewardChallengeRepository challengeRepository;

    public GetRewardChallengeDetailResp getDetail(Long challengeId) {
        RewardChallenge rewardChallenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new Exception404(NOT_FOUND_CHALLENGE_ID));

        return new GetRewardChallengeDetailResp(rewardChallenge);
    }
}
