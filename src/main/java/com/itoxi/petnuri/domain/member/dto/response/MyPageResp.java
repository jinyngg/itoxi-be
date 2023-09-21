package com.itoxi.petnuri.domain.member.dto.response;

import com.itoxi.petnuri.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class MyPageResp {
    private final String profileImageUrl;
    private final String nickname;
    private final String email;

    public MyPageResp(Member member) {
        this.profileImageUrl = member.getProfileImageUrl();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }
}
