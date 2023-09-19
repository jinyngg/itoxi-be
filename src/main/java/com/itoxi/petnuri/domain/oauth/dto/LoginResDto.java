package com.itoxi.petnuri.domain.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResDto {

    private String jwtToken;
    private String refreshToken;
    private String email;
    private String nickname;

    public LoginResDto(String email){
        this.email = email;
    }
}
