package com.itoxi.petnuri.domain.dailychallenge.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * author         : Jisang Lee
 * date           : 2023-09-23
 * description    :
 */
@Getter
@Builder
public class DailyAuthDto {
    private Long challengeId;
    private Long payment;
    private String challengeTitle;
    private String authImageUrl;
}
