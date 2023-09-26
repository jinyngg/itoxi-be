package com.itoxi.petnuri.domain.eventChallenge.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itoxi.petnuri.domain.eventChallenge.type.PointMethod;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class WritePointChallengePostRequest {

    private String title;
    private String subTitle;

    private String notice;

    private Long point;
    private PointMethod pointMethod;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
