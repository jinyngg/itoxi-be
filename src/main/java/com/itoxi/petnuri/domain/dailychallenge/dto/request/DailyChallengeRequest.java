package com.itoxi.petnuri.domain.dailychallenge.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

/**
 * author         : Jisang Lee
 * date           : 2023-09-30
 * description    :
 */
@Getter
public class DailyChallengeRequest {

    private String title;         // 챌린지 이름
    private String subTitle;      // 챌린지 소제목
    private String authMethod;    // 인증 방법
    private Long payment;         // 리워드 포인트
    private String paymentMethod; // 포인트 지급 방법

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;  // 챌린지 시작 일자. 2023-09-30

}
