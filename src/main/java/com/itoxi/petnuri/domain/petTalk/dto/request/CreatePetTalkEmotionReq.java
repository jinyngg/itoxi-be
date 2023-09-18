package com.itoxi.petnuri.domain.petTalk.dto.request;

import com.itoxi.petnuri.domain.petTalk.type.EmojiType;
import com.itoxi.petnuri.global.common.customValid.valid.ValidEnum;
import lombok.Getter;

@Getter
public class CreatePetTalkEmotionReq {
    @ValidEnum(enumClass = EmojiType.class)
    EmojiType emoji;
}