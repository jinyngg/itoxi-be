package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.member.entity.Member;
import java.util.List;

public interface PointChallengeReviewJpaRepositoryCustom {

    List<Member> findMembersForPointReward(PointChallenge pointChallenge, Long reviewCount);
}
