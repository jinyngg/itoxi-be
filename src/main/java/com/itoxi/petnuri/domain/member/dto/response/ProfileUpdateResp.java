package com.itoxi.petnuri.domain.member.dto.response;

import lombok.Getter;

@Getter
public class ProfileUpdateResp {
    private final String nickName;
    private final String profileImageUrl;

    public ProfileUpdateResp(String nickName, String profileImageUrl) {
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
    }
}
