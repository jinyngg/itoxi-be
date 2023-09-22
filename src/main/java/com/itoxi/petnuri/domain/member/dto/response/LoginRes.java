package com.itoxi.petnuri.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginRes {

    private String jwtToken;
    private String jwtRefreshToken;
    private String kakaoToken;
    private String email;

}
