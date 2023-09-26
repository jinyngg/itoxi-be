package com.itoxi.petnuri.domain.eventChallenge.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointMethod {

    DIARY_3(3L, "3회차 반려일기 작성완료시 즉시"),
    DIARY_5(5L, "5회차 반려일기 작성완료시 즉시"),
    DIARY_10(10L, "10회차 반려일기 작성완료시 즉시"),
    ;

    private final Long day;
    private final String description;
}
