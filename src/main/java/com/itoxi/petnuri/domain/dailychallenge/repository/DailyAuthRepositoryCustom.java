package com.itoxi.petnuri.domain.dailychallenge.repository;

/**
 * author         : Jisang Lee
 * date           : 2023-09-26
 * description    :
 */
public interface DailyAuthRepositoryCustom {

    boolean dupeAuthCheck(Long memberId, Long dailyChallengeId);

}
