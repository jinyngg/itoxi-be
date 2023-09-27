package com.itoxi.petnuri.domain.point;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import lombok.*;

/**
 * author         : matrix
 * date           : 2023-09-19
 * description    :
 */
@Getter
@Builder
public class PointGetDto {

    private String challengeName;
    private Long point;

    public static PointGetDto from(DailyChallenge dailyChallenge) {
        return PointGetDto.builder()
                .challengeName(dailyChallenge.getTitle())
                .point(dailyChallenge.getPayment())
                .build();
    }
}
