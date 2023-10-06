package com.itoxi.petnuri.domain.petTalk.dto;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalkView;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmojiCount {

    private Long cuteCount;

    private Long funCount;

    private Long kissCount;

    private Long omgCount;

    private Long sadCount;

    private boolean isCute;

    private boolean isFun;

    private boolean isKiss;

    private boolean isOmg;

    private boolean isSad;

    private Long totalEmojiCount;

    public static EmojiCount fromEntity(PetTalkView petTalkView) {
        return EmojiCount.builder()
                .cuteCount(petTalkView.getCuteCount())
                .funCount(petTalkView.getFunCount())
                .kissCount(petTalkView.getKissCount())
                .omgCount(petTalkView.getOmgCount())
                .sadCount(petTalkView.getSadCount())
                .isCute(petTalkView.isCute())
                .isFun(petTalkView.isFun())
                .isKiss(petTalkView.isKiss())
                .isOmg(petTalkView.isOmg())
                .isSad(petTalkView.isSad())
                .totalEmojiCount(petTalkView.getTotalEmojiCount())
                .build();
    }
}
