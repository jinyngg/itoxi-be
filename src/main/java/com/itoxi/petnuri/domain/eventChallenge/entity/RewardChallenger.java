package com.itoxi.petnuri.domain.eventChallenge.entity;

import com.itoxi.petnuri.domain.eventChallenge.type.RewardChallengerProcess;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static com.itoxi.petnuri.domain.eventChallenge.type.RewardChallengerProcess.APPLY;
import static com.itoxi.petnuri.domain.eventChallenge.type.RewardChallengerProcess.KIT_REVIEW_COMPLETE;

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
    private RewardChallengerProcess process = APPLY;

    @Column(name = "is_consented_personal_info", nullable = false)
    private Boolean isConsentedPersonalInfo;

    public static RewardChallenger create(Member challenger, RewardChallenge rewardChallenge, Boolean isConsentedPersonalInfo) {
        return RewardChallenger.builder()
                .challenger(challenger)
                .rewardChallenge(rewardChallenge)
                .isConsentedPersonalInfo(isConsentedPersonalInfo)
                .build();
    }

    public void reviewComplete() {
        this.process = KIT_REVIEW_COMPLETE;
    }
}
