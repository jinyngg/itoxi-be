package com.itoxi.petnuri.domain.dailychallenge.entity;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * author         : matrix
 * date           : 2023-09-13
 * description    : 데일리 챌린지 인증글
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "daily_auth")
public class DailyAuth extends BaseTimeEntity {

    @Id
    @Column(name = "daily_auth_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "daily_challenge_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private DailyChallenge dailyChallenge;

    @Column(nullable = false)
    private String imageUrl;  // 인증샷 S3 url

    public static DailyAuth toEntity(
            Member member, DailyChallenge dailyChallenge, String imageUrl
    ) {
        return DailyAuth.builder()
                .member(member)
                .dailyChallenge(dailyChallenge)
                .imageUrl(imageUrl)
                .build();
    }

}