package com.itoxi.petnuri.domain.dailychallenge.entity;

import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
/**
 * author         : matrix
 * date           : 2023-09-13
 * description    : 데일리 챌린지 참여
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "daily_participate")
public class DailyParticipate extends BaseTimeEntity {

    @Id
    @Column(name = "daily_participate_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_challenge_id")
    private DailyChallenge dailyChallenge;  // 챌린지 참여

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_auth_id")
    private DailyAuth dailyAuth;

    @Transient
    private Boolean status;     // 인증글 작성 여부
}
