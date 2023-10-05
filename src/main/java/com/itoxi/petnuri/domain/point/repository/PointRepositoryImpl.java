package com.itoxi.petnuri.domain.point.repository;

import com.itoxi.petnuri.domain.member.entity.QMember;
import com.itoxi.petnuri.domain.point.dto.response.PointResponse;
import com.itoxi.petnuri.domain.point.entity.QPoint;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

/**
 * author         : Jisang Lee
 * date           : 2023-10-05
 * description    :
 */
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QPoint point = QPoint.point;
    QMember member = QMember.member;

    @Override
    public PointResponse findPointByMemberId(Long memberId) {
        return queryFactory
                .select(Projections.constructor(PointResponse.class,
                        member.nickname, member.profileImageUrl, point.havePoint))
                .from(point)
                .join(member)
                .on(point.member.id.eq(member.id))
                .where(point.member.id.eq(memberId))
                .fetchOne();
    }

}
