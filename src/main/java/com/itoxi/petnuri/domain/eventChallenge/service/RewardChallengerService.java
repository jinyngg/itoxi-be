package com.itoxi.petnuri.domain.eventChallenge.service;

import com.itoxi.petnuri.domain.eventChallenge.dto.response.GetMyRewardChallengeJoinResp;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenger;
import com.itoxi.petnuri.domain.eventChallenge.repository.RewardChallengerRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_CHALLENGE_JOIN;

@Service
@RequiredArgsConstructor
public class RewardChallengerService {
    private final RewardChallengerRepository rewardChallengerRepository;

    @Transactional(readOnly = true)
    public GetMyRewardChallengeJoinResp getMyJoin(Member member, Long challengeId) {
        RewardChallenger rewardChallenger = rewardChallengerRepository.findByChallengerAndRewardChallengeId(member, challengeId)
                .orElseThrow(() -> new Exception404(NOT_FOUND_CHALLENGE_JOIN));

        return new GetMyRewardChallengeJoinResp(rewardChallenger.getProcess());
    }
}
