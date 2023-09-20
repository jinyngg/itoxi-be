package com.itoxi.petnuri.domain.member.dto.response;

import lombok.Getter;

@Getter
public class KakaoToken {
    private String tokenType;

    private String accessToken;

    private Integer expiresIn;

    private String refreshToken;

    private Integer refreshTokenExpiresIn;

    private String scope;

}
