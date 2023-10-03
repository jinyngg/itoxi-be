package com.itoxi.petnuri.domain.member.dto.response;

import lombok.Getter;

@Getter
public class ExistNickNameResp {
    private final String nickname;
    private final Boolean isExists;

    public ExistNickNameResp(String nickname, Boolean isExists) {
        this.nickname = nickname;
        this.isExists = isExists;
    }
}
