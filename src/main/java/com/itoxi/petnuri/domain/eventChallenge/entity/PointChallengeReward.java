package com.itoxi.petnuri.domain.eventChallenge.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itoxi.petnuri.domain.member.entity.Member;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "point_challenge_reward")
public class PointChallengeReward {

    @Id
    @Column(name = "point_challenge_reward_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_challenge_id")
    private PointChallenge pointChallenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder.Default
    @Column(name = "rewarded_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rewardedAt = LocalDate.now();

}
