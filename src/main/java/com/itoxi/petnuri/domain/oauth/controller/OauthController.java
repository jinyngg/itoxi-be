package com.itoxi.petnuri.domain.oauth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itoxi.petnuri.domain.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/oauth")
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    //프론트에서 넘긴 코드 받고 사용자 정보 넘겨주기
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) throws JsonProcessingException {
        return ResponseEntity.ok(oauthService.kakaoLogin(code));

    }
}
