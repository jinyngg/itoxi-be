package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReward;
import com.itoxi.petnuri.domain.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointChallengeRewardJpaRepository extends JpaRepository<PointChallengeReward, Long> {

    List<PointChallengeReward> findAllByPointChallenge(PointChallenge pointChallenge);

    @Query("SELECT p.member FROM PointChallengeReward p WHERE p.id = :id")
    Member findMemberById(@Param("id") Long id);

}