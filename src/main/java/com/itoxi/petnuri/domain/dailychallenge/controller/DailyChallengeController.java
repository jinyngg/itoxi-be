package com.itoxi.petnuri.domain.dailychallenge.controller;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.service.DailyChallengeService;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * author         : Jisang Lee
 * date           : 2023-09-25
 * description    :
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge/daily")
public class DailyChallengeController {

    private final DailyChallengeService dailyChallengeService;

    @GetMapping
    public ResponseEntity challengeMainView(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();

        List<DailyChallengeListResponse> responses = dailyChallengeService.respDailyChallengeList(member);
        return ResponseEntity.ok(responses);
    }
}
