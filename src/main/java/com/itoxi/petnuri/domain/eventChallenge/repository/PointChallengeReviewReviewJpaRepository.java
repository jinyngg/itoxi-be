package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReview;
import com.itoxi.petnuri.domain.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointChallengeReviewReviewJpaRepository extends JpaRepository<PointChallengeReview, Long> {

    List<PointChallengeReview> findByPointChallengeAndReviewer(
            PointChallenge pointChallenge, Member reviewer);
}
