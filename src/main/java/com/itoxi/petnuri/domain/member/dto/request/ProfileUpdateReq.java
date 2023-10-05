package com.itoxi.petnuri.domain.member.dto.request;

import com.itoxi.petnuri.global.common.customValid.valid.ValidNickName;
import lombok.Getter;

@Getter
public class ProfileUpdateReq {
    @ValidNickName
    private String nickname;
}
