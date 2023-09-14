package com.itoxi.petnuri.domain.petTalk.dto.request;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

@Getter
public class WritePetTalkReplyReq {
    @PositiveOrZero
    private Long parentId;

    @Max(500)
    private String content;
}