package com.itoxi.petnuri.domain.eventChallenge.dto.request;

import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.common.customValid.valid.ValidEnum;
import lombok.Getter;

@Getter
public class WriteReviewReq {
    @ValidEnum(enumClass = PetType.class)
    private PetType petType;

//    @Size(min = 15, max = 500, message = "글자수는 15~500자 이어야 합니다.")
    private String content;
}
