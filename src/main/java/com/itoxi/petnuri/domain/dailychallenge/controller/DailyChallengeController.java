package com.itoxi.petnuri.domain.dailychallenge.controller;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyAuthImageResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeDetailResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.service.DailyChallengeService;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.customValid.valid.ValidId;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity getChallengeMainView(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member loginMember = isLoginMember(principalDetails);

        List<DailyChallengeListResponse> responses = dailyChallengeService.respDailyChallengeList(loginMember);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity getChallengeDetailView(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable @ValidId Long challengeId
    ) {
        Member loginMember = isLoginMember(principalDetails);

        DailyChallengeDetailResponse responses =
                dailyChallengeService.respDailyChallengeDetail(challengeId, loginMember);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{challengeId}/auth")
    public ResponseEntity<?> getAllAuthList(
            @PathVariable @ValidId Long challengeId,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        Page<DailyAuthImageResponse> responses =
                dailyChallengeService.respDailyChallengeAuthList(challengeId, pageable);
        return ResponseEntity.ok(responses);
    }


    private Member isLoginMember(PrincipalDetails principalDetails) {
        if (principalDetails == null) { // NullPointerException 방지
            return null;
        }
        return principalDetails.getMember();
    }

}
