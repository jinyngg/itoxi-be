package com.itoxi.petnuri.domain.dailychallenge.dto;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyAuth;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
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

    public static DailyAuthDto of(DailyAuth dailyAuth, DailyChallenge dailyChallenge) {
        return DailyAuthDto.builder()
                .challengeId(dailyChallenge.getId())
                .payment(dailyChallenge.getPayment())
                .challengeTitle(dailyChallenge.getTitle())
                .authImageUrl(dailyAuth.getImageUrl())
                .build();
    }
    
}
