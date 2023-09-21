package com.itoxi.petnuri.domain.member.dto.response;

import com.itoxi.petnuri.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class JoinResp {
    private final Long id;
    private final String email;
    private final String nickName;
    private final String jwtToken;
    private final String refreshToken;

    public JoinResp(Member member, String jwtToken, String refreshToken) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickName = member.getNickname();
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }
}
