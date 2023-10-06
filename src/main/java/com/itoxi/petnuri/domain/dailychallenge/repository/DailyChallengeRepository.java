package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.type.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * author         : matrix
 * date           : 2023-09-16
 * description    :
 */
public interface DailyChallengeRepository extends JpaRepository<DailyChallenge, Long>, DailyChallengeRepositoryCustom {

    boolean existsByIdAndChallengeStatus(Long challengeId, ChallengeStatus status);

    Optional<DailyChallenge> findByIdAndChallengeStatus(Long challengeId, ChallengeStatus status);

}
