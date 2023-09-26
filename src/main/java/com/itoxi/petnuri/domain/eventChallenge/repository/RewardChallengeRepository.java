package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RewardChallengeRepository extends JpaRepository<RewardChallenge, Long> {

    @Query("SELECT rc FROM RewardChallenge rc " +
            "LEFT JOIN rc.rewardChallengers rch " +
            "GROUP BY rc.id " +
            "ORDER BY COUNT(rch) DESC")
    List<RewardChallenge> findTop2ByOrderByRewardChallengersDesc(Pageable pageable);
}
