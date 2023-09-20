package com.itoxi.petnuri.domain.member.controller;

import com.itoxi.petnuri.domain.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //프론트에서 넘긴 코드 받고 사용자 정보 넘겨주기
    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code){
        return ResponseEntity.ok(authService.kakaoLogin(code));

    }
}
