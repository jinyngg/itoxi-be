package com.itoxi.petnuri.domain.eventChallenge.dto.request;

import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.common.customValid.valid.ValidEnum;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
public class WriteReviewReq {
    @ValidEnum(enumClass = PetType.class)
    private PetType petType;

    @Min(15)
    @Max(500)
    private String content;
}
