package com.itoxi.petnuri.domain.product.entity;

import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.product.type.ChallengeProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "challenge_product")
public class ChallengeProduct {
    @Id
    @Column(name = "challenge_product_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_challenge_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RewardChallenge rewardChallenge;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeProductCategory category;
}
