package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.entity.QDailyAuth;
import com.itoxi.petnuri.domain.dailychallenge.entity.QDailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.type.ChallengeStatus;
import com.itoxi.petnuri.domain.dailychallenge.util.QuerydslDateTimeFormatter;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * author         : Jisang Lee
 * date           : 2023-09-25
 * description    :
 */
@RequiredArgsConstructor
public class DailyChallengeRepositoryImpl implements DailyChallengeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QuerydslDateTimeFormatter querydslFormatter;
    QDailyChallenge dailyChallenge = QDailyChallenge.dailyChallenge;
    QDailyAuth dailyAuth = QDailyAuth.dailyAuth;

    // 챌린지 메인 화면 데일리 챌린지 화면 파트 응답용
    @Override
    public List<DailyChallengeListResponse> listChallenge(Member loginMember) {

        StringExpression authDate = querydslFormatter.formatter(dailyAuth.updatedAt);
        String todayStr = querydslFormatter.nowDateTimeToStr();

        return queryFactory
                .select(Projections.constructor(DailyChallengeListResponse.class,
                        dailyChallenge.id, dailyChallenge.title,
                        dailyChallenge.thumbnail,
                        dailyAuth.member.isNotNull()))
                .from(dailyChallenge)
                .leftJoin(dailyAuth)
                .on(dailyChallenge.id.eq(dailyAuth.dailyChallenge.id)
                        .and(dailyAuth.member.id.eq(loginMember.getId()))
                        .and(authDate.eq(todayStr)))
                .where(dailyChallenge.challengeStatus.eq(ChallengeStatus.OPENED))
                .fetch();
    }

}
