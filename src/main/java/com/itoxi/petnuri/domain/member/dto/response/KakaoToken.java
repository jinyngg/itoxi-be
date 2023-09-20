package com.itoxi.petnuri.domain.member.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoToken {
    private String tokenType;

    private String accessToken;

    private Integer expiresIn;

    private String refreshToken;

    private Integer refreshTokenExpiresIn;

    private String scope;

}
