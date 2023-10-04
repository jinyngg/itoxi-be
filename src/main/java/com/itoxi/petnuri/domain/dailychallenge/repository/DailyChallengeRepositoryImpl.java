package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyAuthImageResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeDetailResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.entity.QDailyAuth;
import com.itoxi.petnuri.domain.dailychallenge.entity.QDailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.type.ChallengeStatus;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
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

    // 챌린지 메인 화면 : 데일리 챌린지 list view 응답
    @Override
    public List<DailyChallengeListResponse> findAllAuthStatus(Member loginMember) {

        return queryFactory
                .select(Projections.constructor(DailyChallengeListResponse.class,
                        dailyChallenge.id, dailyChallenge.title,
                        dailyChallenge.thumbnail, dailyAuth.member.isNotNull()))
                .from(dailyChallenge)
                .leftJoin(dailyAuth)
                .on(dailyChallenge.id.eq(dailyAuth.dailyChallenge.id)
                        .and(memberEq(dailyAuth.member, loginMember))
                        .and(goeToday(dailyAuth.updatedAt)))
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
                        .and(goeToday(dailyAuth.updatedAt)))
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
                        .and(goeToday(dailyAuth.updatedAt)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(dailyAuth.count())
                .from(dailyAuth)
                .where(dailyAuth.dailyChallenge.id.eq(dailyChallengeId)
                        .and(goeToday(dailyAuth.updatedAt)));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public long updateDailyChallenge() {
        long result = queryFactory
                .update(dailyChallenge)
                .set(dailyChallenge.challengeStatus, ChallengeStatus.OPENED)
                .where(dailyChallenge.challengeStatus.eq(ChallengeStatus.READY)
                        .and(eqToday(dailyChallenge.startDate)))
                .execute();
        return result;
    }


    // 로그인 하지 않은 유저는 존재할 수 없는 id 0과 비교.
    private BooleanExpression memberEq(QMember qMember, Member loginMember) {
        if (loginMember == null) {
            return qMember.id.eq(0L);
        }
        return qMember.id.eq(loginMember.getId());
    }

    private BooleanExpression goeToday(DateTimePath<LocalDateTime> compareDate) {
        return compareDate.goe(LocalDate.now().atStartOfDay());
    }

    private BooleanExpression eqToday(DatePath<LocalDate> startDate) {
        return startDate.eq(LocalDate.now());
    }

}
