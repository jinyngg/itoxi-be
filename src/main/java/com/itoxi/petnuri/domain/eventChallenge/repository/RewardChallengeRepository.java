package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RewardChallengeRepository extends JpaRepository<RewardChallenge, Long> {

    @Query("SELECT rc, COUNT(rch) AS challenger " +
            "FROM RewardChallenge rc " +
            "LEFT JOIN RewardChallenger rch ON rc.id = rch.rewardChallenge.id " +
            "WHERE rc.status = 'OPENED'" +
            "GROUP BY rc.id " +
            "ORDER BY challenger DESC")
    List<RewardChallenge> findTop2ByOrderByRewardChallengersDesc(Pageable pageable);
}
