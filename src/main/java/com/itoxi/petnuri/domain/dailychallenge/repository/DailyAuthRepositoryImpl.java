package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.entity.QDailyAuth;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    QDailyAuth dailyAuth = QDailyAuth.dailyAuth;

    @Override
    public boolean dupeAuthCheck(Long loginMemberId, Long dailyChallengeId) {
        return queryFactory
                .selectOne()
                .from(dailyAuth)
                .where(
                        dailyAuth.dailyChallenge.id.eq(dailyChallengeId),
                        dailyAuth.member.id.eq(loginMemberId),
                        goeToday(dailyAuth.updatedAt)
                )
                .fetchFirst() != null; // true : 중복 인증
    }

    private BooleanExpression goeToday(DateTimePath<LocalDateTime> compareDate) {
        return compareDate.goe(LocalDate.now().atStartOfDay());
    }

}
