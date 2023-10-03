package com.itoxi.petnuri.domain.member.controller;

import com.itoxi.petnuri.domain.member.dto.request.JoinReq;
import com.itoxi.petnuri.domain.member.dto.response.ExistNickNameResp;
import com.itoxi.petnuri.domain.member.dto.response.ExistReferralCodeResp;
import com.itoxi.petnuri.domain.member.dto.response.ReissueResp;
import com.itoxi.petnuri.domain.member.dto.response.JoinResp;
import com.itoxi.petnuri.domain.member.service.AuthService;
import com.itoxi.petnuri.global.common.customValid.valid.ValidNickName;
import com.itoxi.petnuri.global.security.jwt.JwtTokenProvider;
import com.itoxi.petnuri.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RequestMapping("/auth")
@RestController
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    //프론트에서 넘긴 코드 받고 사용자 정보 넘겨주기
    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code){
        return ResponseEntity.ok(authService.kakaoLogin(code));
    }

    /* 토큰 재발급*/
    @GetMapping("/reissue")
    public ResponseEntity<ReissueResp> reissue(
            @RequestHeader(JwtTokenProvider.HEADER) String accessToken,
            @CookieValue(CookieUtil.NAME_REFRESH_TOKEN) String refreshToken
    ) {
        accessToken = jwtTokenProvider.resolveToken(accessToken);
        ReissueResp response = authService.reissue(accessToken, refreshToken);
        HttpHeaders headers = getCookieHeaders(response.getRefreshToken());
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }

    @PostMapping("/join")
    public ResponseEntity<JoinResp> join(
            @RequestBody JoinReq request,
            Errors errors
    ) {
        JoinResp response = authService.join(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/nickname")
    public ResponseEntity<ExistNickNameResp> checkNickName(
            @RequestParam @ValidNickName String nickname
    ) {
        ExistNickNameResp response = authService.checkNickName(nickname);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/referral")
    public ResponseEntity<ExistReferralCodeResp> checkReferralCode(
            @RequestParam @NotBlank String referralCode
    ) {
        ExistReferralCodeResp response = authService.checkReferralCode(referralCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private HttpHeaders getCookieHeaders(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = CookieUtil.getRefreshTokenCookie(refreshToken);
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return headers;
    }
}
