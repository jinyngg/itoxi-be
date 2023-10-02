package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReview;
import com.itoxi.petnuri.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointChallengeReviewJpaRepository extends
        JpaRepository<PointChallengeReview, Long>, PointChallengeReviewJpaRepositoryCustom {

    List<PointChallengeReview> findByPointChallengeAndReviewer(
            PointChallenge pointChallenge, Member reviewer);

    @Query("SELECT " +
            "CASE WHEN COUNT(review) > 0 " +
            "THEN true ELSE false " +
            "END " +
            "FROM PointChallengeReview review " +
            "WHERE DATE(review.createdAt) = DATE(CURRENT_TIMESTAMP) " +
            "AND review.pointChallenge = :pointChallenge " +
            "AND review.reviewer = :reviewer")
    boolean existsTodayReviewByPointChallengeAndReviewer(
            @Param("pointChallenge") PointChallenge pointChallenge,
            @Param("reviewer") Member member);

    @Query("SELECT review " +
            "FROM PointChallengeReview review " +
            "WHERE review.pointChallenge = :pointChallenge " +
            "AND review.reviewer = :reviewer " +
            "ORDER BY review.createdAt DESC")
    Optional<PointChallengeReview> findLatestPointChallengeReviewByPointChallengeAndMember(
            @Param("pointChallenge") PointChallenge pointChallenge,
            @Param("reviewer") Member member);

}
