package com.itoxi.petnuri.domain.product.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.product.entity.ChallengeProduct;
import com.itoxi.petnuri.domain.product.type.ChallengeProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeProductRepository extends JpaRepository<ChallengeProduct, Long> {
    Optional<ChallengeProduct> findByRewardChallengeAndCategory(RewardChallenge rewardChallenge, ChallengeProductCategory challengeProductCategory);

    List<ChallengeProduct> findAllByRewardChallengeAndCategory(RewardChallenge rewardChallenge, ChallengeProductCategory challengeProductCategory);
}
