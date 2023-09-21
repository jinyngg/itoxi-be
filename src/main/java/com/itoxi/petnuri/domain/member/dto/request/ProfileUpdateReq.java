package com.itoxi.petnuri.domain.member.dto.request;

import com.itoxi.petnuri.global.common.customValid.valid.ValidNickName;
import lombok.Getter;

@Getter
public class ProfileUpdateReq {
    @ValidNickName
    private final String nickName;

    public ProfileUpdateReq(String nickName) {
        this.nickName = nickName;
    }
}
