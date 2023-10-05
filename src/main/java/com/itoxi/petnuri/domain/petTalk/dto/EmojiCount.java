package com.itoxi.petnuri.domain.petTalk.dto;

import com.itoxi.petnuri.domain.member.dto.Writer;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkView;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;

@Getter
@Builder
public class EmojiCount {

    private Long cuteCount;

    private Long funCount;

    private Long kissCount;

    private Long omgCount;

    private Long sadCount;

    private Long totalEmojiCount;

    public static EmojiCount fromEntity(PetTalkView petTalkView) {
        return EmojiCount.builder()
                .cuteCount(petTalkView.getCuteCount())
                .funCount(petTalkView.getFunCount())
                .kissCount(petTalkView.getKissCount())
                .omgCount(petTalkView.getOmgCount())
                .sadCount(petTalkView.getSadCount())
                .totalEmojiCount(petTalkView.getTotalEmojiCount())
                .build();
    }
}
