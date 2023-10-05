package com.itoxi.petnuri.domain.petTalk.dto.request;

import lombok.Getter;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
public class WritePetTalkReplyReq {
    @PositiveOrZero
    private Long parentId;

    @Size(max = 500, message = "글자수는 500자 이하여야 합니다.")
    private String content;
}