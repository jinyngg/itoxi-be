package com.itoxi.petnuri.domain.eventChallenge.dto.request;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
public class WriteReviewReq {
    @Min(15)
    @Max(500)
    private String content;
}
