package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallengeReview;
import com.itoxi.petnuri.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardChallengeReviewRepository extends JpaRepository<RewardChallengeReview, Long> {
    boolean existsByChallengerAndRewardChallengeId(Member challenger, Long rewardChallengeId);

    List<RewardChallengeReview> findAllByRewardChallenge(RewardChallenge rewardChallenge);
}
