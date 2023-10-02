package com.itoxi.petnuri.domain.eventChallenge.repository.impl;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.QPointChallengeReview;
import com.itoxi.petnuri.domain.eventChallenge.entity.QPointChallengeReward;
import com.itoxi.petnuri.domain.eventChallenge.repository.PointChallengeReviewJpaRepositoryCustom;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointChallengeReviewJpaRepositoryCustomImpl implements
        PointChallengeReviewJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QPointChallengeReview qPointChallengeReview = QPointChallengeReview.pointChallengeReview;
    QPointChallengeReward qPointChallengeReward = QPointChallengeReward.pointChallengeReward;

    @Override
    public List<Member> findMembersForPointReward(PointChallenge pointChallenge, Long reviewCount) {
        List<Member> excludedReviewers = queryFactory
                .select(qPointChallengeReward.member)
                .from(qPointChallengeReward)
                .where(eqPointChallengeInReward(pointChallenge))
                .fetch();

        return queryFactory
                .select(qPointChallengeReview.reviewer)
                .from(qPointChallengeReview)
                .where(eqPointChallenge(pointChallenge)
                        .and(notInReviewers(excludedReviewers)))
                .groupBy(qPointChallengeReview.reviewer)
                .having(goeReviewCount(reviewCount))
                .fetch();
    }

    private BooleanExpression notInReviewers(List<Member> excludedReviewers) {
        return qPointChallengeReview.reviewer.notIn(excludedReviewers);
    }

    private BooleanExpression eqPointChallenge(PointChallenge pointChallenge) {
        return qPointChallengeReview.pointChallenge.eq(pointChallenge);
    }

    private BooleanExpression eqPointChallengeInReward(PointChallenge pointChallenge) {
        return qPointChallengeReward.pointChallenge.eq(pointChallenge);
    }

    private BooleanExpression goeReviewCount(Long reviewCount) {
        return qPointChallengeReview.count().goe(reviewCount);
    }

}
