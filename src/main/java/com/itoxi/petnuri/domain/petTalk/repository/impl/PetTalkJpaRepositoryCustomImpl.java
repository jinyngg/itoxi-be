package com.itoxi.petnuri.domain.petTalk.repository.impl;

import static com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus.ACTIVE;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkView;
import com.itoxi.petnuri.domain.petTalk.entity.QPetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.QPetTalkEmotion;
import com.itoxi.petnuri.domain.petTalk.entity.QPetTalkView;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkJpaRepositoryCustom;
import com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PetTalkJpaRepositoryCustomImpl implements PetTalkJpaRepositoryCustom {

    private static final int RECENT_DAYS = 3;

    private final JPAQueryFactory queryFactory;

    QPetTalk qPetTalk = QPetTalk.petTalk;
    QPetTalkView qPetTalkView = QPetTalkView.petTalkView;

    @Override
    public Page<PetTalkView> loadLatestPetTalkViewsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        Pageable pageable = PageRequest.of(page, size);

        List<PetTalkView> petTalkViews = queryFactory
                .selectFrom(qPetTalkView)
                .where(eqViewActive()
                        .and(eqViewMainCategory(mainCategoryId))
                        .and(eqViewSubCategory(subCategoryId))
                        .and(eqViewPetType(petType)))
                .orderBy(qPetTalkView.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = petTalkViews.size();

        return PageableExecutionUtils.getPage(petTalkViews, pageable, () -> totalCount);
    }

    @Override
    public Page<PetTalkView> loadBestPetTalkViewsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        Pageable pageable = PageRequest.of(page, size);

        List<PetTalkView> petTalkViews = queryFactory
                .selectFrom(qPetTalkView)
                .where(eqViewActive()
                        .and(eqViewMainCategory(mainCategoryId))
                        .and(eqViewSubCategory(subCategoryId))
                        .and(eqViewPetType(petType))
                        .and(recentlyCreated()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = petTalkViews.size();

        return PageableExecutionUtils.getPage(petTalkViews, pageable, () -> totalCount);
    }

    @Override
    public Optional<PetTalkView> loadPetTalkPostsDetails(Long petTalkId) {
//        return Optional.ofNullable(
//                queryFactory
//                        .selectFrom(qPetTalk)
//                        .where(eqActive()
//                                .and(eqPetTalkId(petTalkId)))
//                        .fetchOne());
        return Optional.ofNullable(queryFactory
                .selectFrom(qPetTalkView)
                .where(eqViewActive()
                        .and(eqViewPetTalkId(petTalkId)))
                .fetchOne());
    }

    @Override
    public void addViewCountFromRedis(Long petTalkId, Long petTalkViewCount) {
        queryFactory
                .update(qPetTalk)
                .set(qPetTalk.viewCount, petTalkViewCount)
                .where(eqPetTalkId(petTalkId))
                .execute();
    }


    private BooleanExpression eqPetTalkId(Long petTalkId) {
        return qPetTalk.id.eq(petTalkId);
    }

    private BooleanExpression eqViewPetTalkId(Long petTalkId) {
        return qPetTalkView.petTalkId.eq(petTalkId);
    }

    private BooleanExpression eqViewActive() {
        return qPetTalkView.status.eq(ACTIVE);
    }

    private BooleanExpression eqViewPetType(PetType petType) {
        return qPetTalkView.petType.eq(petType);
    }

    private BooleanExpression eqViewMainCategory(Long mainCategoryId) {
        if (mainCategoryId == null) {
            return null;
        }

        return qPetTalkView.mainCategoryId.eq(mainCategoryId);
    }

    private BooleanExpression eqViewSubCategory(Long subCategoryId) {
        if (subCategoryId == null) {
            return null;
        }

        return qPetTalkView.subCategoryId.eq(subCategoryId);
    }

    private BooleanExpression recentlyCreated() {
        return qPetTalkView.createdAt.after(LocalDateTime.now().minusDays(RECENT_DAYS));
    }
}
