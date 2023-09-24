package com.itoxi.petnuri.domain.eventChallenge.dto.response;

import com.itoxi.petnuri.domain.eventChallenge.type.RewardChallengerProcess;
import lombok.Getter;

@Getter
public class GetMyRewardChallengeJoinResp {
    private final RewardChallengerProcess process;

    public GetMyRewardChallengeJoinResp(RewardChallengerProcess process) {
        this.process = process;
    }
}
