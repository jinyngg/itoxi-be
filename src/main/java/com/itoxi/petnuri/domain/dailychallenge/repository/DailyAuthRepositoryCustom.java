package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.member.entity.Member;

/**
 * author         : Jisang Lee
 * date           : 2023-09-26
 * description    :
 */
public interface DailyAuthRepositoryCustom {

    boolean dupeAuthCheck(Long memberId, Long dailyChallengeId);

    Long deleteAuthDataByDate();

}
