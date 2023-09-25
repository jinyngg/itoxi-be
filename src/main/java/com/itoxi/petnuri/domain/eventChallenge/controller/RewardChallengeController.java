package com.itoxi.petnuri.domain.eventChallenge.controller;

import com.itoxi.petnuri.domain.eventChallenge.dto.response.GetRewardChallengeDetailResp;
import com.itoxi.petnuri.domain.eventChallenge.service.RewardChallengeService;
import com.itoxi.petnuri.global.common.customValid.valid.ValidId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenge/reward")
@RequiredArgsConstructor
public class RewardChallengeController {
    private final RewardChallengeService challengeService;

    @GetMapping("/{challengeId}")
    public ResponseEntity<GetRewardChallengeDetailResp> getDetail(@PathVariable @ValidId Long challengeId) {
        GetRewardChallengeDetailResp response = challengeService.getDetail(challengeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
