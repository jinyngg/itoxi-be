package com.itoxi.petnuri.domain.dailychallenge.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itoxi.petnuri.domain.dailychallenge.type.ChallengeStatus;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * author         : matrix
 * date           : 2023-09-13
 * description    : 데일리 챌린지
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "daily_challenge")
public class DailyChallenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_challenge_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;       // 챌린지명

    @Column(nullable = false)
    private String subTitle;    // 챌린지 소제목


    @Column(name = "auth_method", nullable = false)
    private String authMethod; // 챌린지 인증 방법

    @Column(nullable = false)
    private Long payment;      // 챌린지 인증 완료시 지급 포인트

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod; // 챌린지 인증 완료시 지급 포인트

    @Column(nullable = false)
    private String thumbnail;   // S3 url

    @Column(nullable = false)
    private String banner;      // S3 url

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private ChallengeStatus challengeStatus = ChallengeStatus.READY;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;    // 챌린지 시작 일자 : 2023-09-12 00:00:00

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDate;      // 챌린지 종료 일자 : 9999-12-31 23:59:59



}
