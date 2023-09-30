package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyAuthImageResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeDetailResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.entity.QDailyAuth;
import com.itoxi.petnuri.domain.dailychallenge.entity.QDailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.type.ChallengeStatus;
import com.itoxi.petnuri.domain.dailychallenge.util.QuerydslDateTimeFormatter;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * author         : Jisang Lee
 * date           : 2023-09-25
 * description    :
 */
@RequiredArgsConstructor
public class DailyChallengeRepositoryImpl implements DailyChallengeRepositoryCustom {

    QDailyChallenge dailyChallenge = QDailyChallenge.dailyChallenge;
    QDailyAuth dailyAuth = QDailyAuth.dailyAuth;
    QMember member = QMember.member;

    private final JPAQueryFactory queryFactory;
    private final QuerydslDateTimeFormatter querydslFormatter;

    // 챌린지 메인 화면 데일리 챌린지 화면 파트 응답용
    @Override
    public List<DailyChallengeListResponse> findAllWithAuthStatus(Member loginMember) {

        return queryFactory
                .select(Projections.constructor(DailyChallengeListResponse.class,
                        dailyChallenge.id, dailyChallenge.title,
                        dailyChallenge.thumbnail, dailyAuth.member.isNotNull()))
                .from(dailyChallenge)
                .leftJoin(dailyAuth)
                .on(dailyChallenge.id.eq(dailyAuth.dailyChallenge.id)
                        .and(memberEq(dailyAuth.member, loginMember))
                        .and(todayEq(dailyAuth.updatedAt)))
                .where(dailyChallenge.challengeStatus.eq(ChallengeStatus.OPENED))
                .fetch();
    }

    @Override
    public DailyChallengeDetailResponse findDetailChallenge(
            Long dailyChallengeId, Member loginMember
    ) {
        return queryFactory
                .select(Projections.constructor(DailyChallengeDetailResponse.class,
                        dailyChallenge.id, dailyChallenge.banner, dailyChallenge.title,
                        dailyChallenge.subTitle, dailyChallenge.authMethod, dailyChallenge.payment,
                        dailyChallenge.paymentMethod, dailyAuth.member.isNotNull()))
                .from(dailyChallenge)
                .leftJoin(dailyAuth)
                .on(dailyChallenge.id.eq(dailyAuth.dailyChallenge.id)
                        .and(memberEq(dailyAuth.member, loginMember))
                        .and(todayEq(dailyAuth.updatedAt)))
                .where(dailyChallenge.id.eq(dailyChallengeId)
                        .and(dailyChallenge.challengeStatus.eq(ChallengeStatus.OPENED)))
                .fetchOne();
    }

    @Override
    public Page<DailyAuthImageResponse> findAllAuthList(
            Long dailyChallengeId, Pageable pageable) {

        List<DailyAuthImageResponse> content = queryFactory
                .select(Projections.constructor(DailyAuthImageResponse.class,
                        dailyAuth.dailyChallenge.id, dailyAuth.member.id,
                        member.nickname, dailyAuth.imageUrl))
                .from(dailyAuth)
                .join(member) // cross join 방지
                .on(dailyAuth.member.id.eq(member.id))
                .where(dailyAuth.dailyChallenge.id.eq(dailyChallengeId)
                        .and(todayEq(dailyAuth.updatedAt)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(dailyAuth.count())
                .from(dailyAuth)
                .where(dailyAuth.dailyChallenge.id.eq(dailyChallengeId)
                        .and(todayEq(dailyAuth.updatedAt)));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


    // 회원이 로그인 하지 않았을 때 on 절의 회원 비교를 하지 않는다. null 반환 = 비교 안함.
    private BooleanExpression memberEq(QMember qMember, Member loginMember) {
        return loginMember != null ? qMember.id.eq(loginMember.getId()) : null;
    }

    // LocalDateTime 에서 시간 부분을 제외하고 날짜만 비교
    private BooleanExpression todayEq(DateTimePath<LocalDateTime> dailyAuth) {
        DateTemplate<LocalDate> authDate = querydslFormatter.formatter(dailyAuth);
        return authDate.eq(LocalDate.now());
    }

}
