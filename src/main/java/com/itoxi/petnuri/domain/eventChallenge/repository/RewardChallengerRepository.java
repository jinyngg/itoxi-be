package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenger;
import com.itoxi.petnuri.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RewardChallengerRepository extends JpaRepository<RewardChallenger, Long> {
    Optional<RewardChallenger> findByChallengerAndRewardChallengeId(Member challenger, Long rewardChallengeId);

    boolean existsByChallengerAndRewardChallenge(Member challenger, RewardChallenge rewardChallenge);
}
