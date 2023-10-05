package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.type.PointChallengeStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointChallengeJpaRepository extends JpaRepository<PointChallenge, Long> {

    @Query("SELECT challenge FROM PointChallenge challenge WHERE challenge.status != 'CLOSED'")
    List<PointChallenge> findAllExceptClosed();

    List<PointChallenge> findAllBySavedAndStatus(boolean isSaved, PointChallengeStatus status);

}
