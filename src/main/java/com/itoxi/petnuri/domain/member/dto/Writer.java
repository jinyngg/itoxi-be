package com.itoxi.petnuri.domain.member.dto;

import com.itoxi.petnuri.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Writer {

    private Long id;
    private String nickname;
    private String profileImageUrl;

    public static Writer fromEntity(Member member) {
        return Writer.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }

}
