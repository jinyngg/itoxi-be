package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.entity.QDailyAuth;
import com.itoxi.petnuri.domain.dailychallenge.util.QuerydslDateTimeFormatter;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

/**
 * author         : Jisang Lee
 * date           : 2023-09-26
 * description    :
 */
@RequiredArgsConstructor
public class DailyAuthRepositoryImpl implements DailyAuthRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QuerydslDateTimeFormatter querydslFormatter;

    QDailyAuth dailyAuth = QDailyAuth.dailyAuth;

    @Override
    public boolean dupePostCheck(Member loginMember, DailyChallenge dailyChallenge) {
        StringExpression authDate = querydslFormatter.formatter(dailyAuth.updatedAt);
        String todayStr = querydslFormatter.nowDateTimeToStr();

        Member result = queryFactory
                .select(dailyAuth.member)
                .from(dailyAuth)
                .where(dailyAuth.id.eq(dailyChallenge.getId())
                        .and(dailyAuth.member.id.eq(loginMember.getId()))
                        .and(authDate.eq(todayStr)))
                .fetchOne();
        return (result != null) ? true : false; // true : 중복 인증
    }

}
