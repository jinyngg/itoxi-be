package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.entity.QDailyAuth;
import com.itoxi.petnuri.domain.dailychallenge.util.QuerydslDateTimeFormatter;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    public boolean dupeAuthCheck(Long loginMemberId, Long dailyChallengeId) {
        Member result = queryFactory
                .select(dailyAuth.member)
                .from(dailyAuth)
                .where(dailyAuth.dailyChallenge.id.eq(dailyChallengeId)
                        .and(dailyAuth.member.id.eq(loginMemberId))
                        .and(eqToday(dailyAuth.updatedAt)))
                .fetchOne();
        return (result != null) ? true : false; // true : 중복 인증
    }

    @Override
    public Long deleteAuthDataByDate() {
        return queryFactory
                .delete(dailyAuth)
                .where(ltToday(dailyAuth.updatedAt))
                .execute();
    }

    private BooleanExpression eqToday(DateTimePath<LocalDateTime> dailyAuth) {
        DateTemplate<LocalDate> authDate = querydslFormatter.formatter(dailyAuth);
        return authDate.eq(LocalDate.now());
    }

    private BooleanExpression ltToday(DateTimePath<LocalDateTime> dailyAuth) {
        DateTemplate<LocalDate> authDate = querydslFormatter.formatter(dailyAuth);
        return authDate.lt(LocalDate.now());
    }

}
