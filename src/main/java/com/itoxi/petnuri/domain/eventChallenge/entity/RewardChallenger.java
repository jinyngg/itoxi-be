package com.itoxi.petnuri.domain.eventChallenge.entity;

import com.itoxi.petnuri.domain.eventChallenge.type.RewardChallengeProcess;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static com.itoxi.petnuri.domain.eventChallenge.type.RewardChallengeProcess.APPLY;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reward_challenger")
public class RewardChallenger extends BaseTimeEntity {

    @Id
    @Column(name = "reward_challenger_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member challenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_challenge_id")
    private RewardChallenge rewardChallenge;

    @Builder.Default
    @Column(name = "process", nullable = false)
    @Enumerated(EnumType.STRING)
    private RewardChallengeProcess process = APPLY;
}
