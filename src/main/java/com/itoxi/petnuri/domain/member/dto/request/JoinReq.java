package com.itoxi.petnuri.domain.member.dto.request;

import com.itoxi.petnuri.global.common.customValid.valid.ValidNickName;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class JoinReq {
    @Email
    private String email;

    @ValidNickName
    private String nickname;

    private String referralCode;
}
