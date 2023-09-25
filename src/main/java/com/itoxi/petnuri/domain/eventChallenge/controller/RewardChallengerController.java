package com.itoxi.petnuri.domain.eventChallenge.controller;

import com.itoxi.petnuri.domain.eventChallenge.dto.request.CreateChallengerReq;
import com.itoxi.petnuri.domain.eventChallenge.dto.response.GetMyRewardChallengeJoinResp;
import com.itoxi.petnuri.domain.eventChallenge.service.RewardChallengerService;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.customValid.valid.ValidId;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/challenge/reward")
@RequiredArgsConstructor
public class RewardChallengerController {
    private final RewardChallengerService rewardChallengerService;

    @GetMapping("/{challengeId}/join/my")
    public ResponseEntity<Object> getMyJoin(
            @PathVariable @ValidId Long challengeId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Member member = principalDetails.getMember();
        GetMyRewardChallengeJoinResp response = rewardChallengerService.getMyJoin(member, challengeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{challengeId}/join")
    public ResponseEntity<Object> create(
            @PathVariable @ValidId Long challengeId,
            @RequestBody @Valid CreateChallengerReq request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Errors errors
    ) {
        Member member = principalDetails.getMember();
        rewardChallengerService.create(member, challengeId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
