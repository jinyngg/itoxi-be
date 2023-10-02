package com.itoxi.petnuri.domain.petTalk.repository.impl;

import static com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus.ACTIVE;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.QPetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.QPetTalkEmotion;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkJpaRepositoryCustom;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    QPetTalkEmotion qPetTalkEmotion = QPetTalkEmotion.petTalkEmotion;

    @Override
    public Page<PetTalk> loadLatestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        Pageable pageable = PageRequest.of(page, size);

        List<PetTalk> petTalks = queryFactory
                .selectFrom(qPetTalk)
                .where(eqActive()
                        .and(eqMainCategory(mainCategoryId))
                        .and(eqSubCategory(subCategoryId))
                        .and(eqPetType(petType)))
                .orderBy(qPetTalk.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = petTalks.size();

        return PageableExecutionUtils.getPage(petTalks, pageable, () -> totalCount);
    }

    @Override
    public Page<PetTalk> loadBestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        Pageable pageable = PageRequest.of(page, size);

//        Expression<Long> emojiCountSubquery = JPAExpressions.select(qPetTalkEmotion.petTalk.count())
//                .from(qPetTalkEmotion)
//                .where(qPetTalkEmotion.petTalk.eq(qPetTalk));
//
//        List<PetTalk> petTalks = queryFactory
//                .selectFrom(qPetTalk)
//                .leftJoin(qPetTalkEmotion).on(qPetTalk.id.eq(qPetTalkEmotion.petTalk.id))
//                .where(eqActive()
//                        .and(qPetTalk.createdAt.after(LocalDateTime.now().minusDays(3)))
//                        .and(qPetTalk.petType.eq(petType)))
//                .orderBy(new OrderSpecifier<>(Order.DESC, emojiCountSubquery)) // Order by the calculated score
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();

        List<PetTalk> petTalks = queryFactory
                .selectFrom(qPetTalk)
                .where(eqActive()
                        .and(recentlyCreated())
                        .and(eqPetType(petType)))
                .fetch();

        // Ranking Algorithm
        List<PetTalk> sortedPetTalks = petTalks.stream()
                .sorted((current, next) -> {
                    Long currentEmojiCount = current.getEmojiCount();
                    Long nextEmojiCount = next.getEmojiCount();

                    LocalDateTime currentCreatedAt = current.getCreatedAt();
                    LocalDateTime nextCreatedAt = next.getCreatedAt();

                    long currentHoursElapsed = Duration.between(currentCreatedAt, LocalDateTime.now()).toHours();
                    long nextHoursElapsed = Duration.between(nextCreatedAt, LocalDateTime.now()).toHours();

                    double currentScore = (currentEmojiCount - 1) / Math.pow(currentHoursElapsed + 2, 2);
                    double nextScore = (nextEmojiCount - 1) / Math.pow(nextHoursElapsed + 2, 2);

                    return Double.compare(nextScore, currentScore);
                })
                .collect(Collectors.toList());

        // Paging
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedPetTalks.size());
        List<PetTalk> pagePetTalks = sortedPetTalks.subList(start, end);

        long totalCount = petTalks.size();

        return PageableExecutionUtils.getPage(pagePetTalks, pageable, () -> totalCount);
    }

    @Override
    public Optional<PetTalk> loadPetTalkPostsDetails(Long petTalkId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qPetTalk)
                        .where(eqActive()
                                .and(eqPetTalkId(petTalkId)))
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

    private BooleanExpression recentlyCreated() {
        return qPetTalk.createdAt.after(LocalDateTime.now().minusDays(RECENT_DAYS));
    }

    private BooleanExpression eqActive() {
        return qPetTalk.status.eq(ACTIVE);
    }

    private BooleanExpression eqPetType(PetType petType) {
        return qPetTalk.petType.eq(petType);
    }

    private BooleanExpression eqMainCategory(Long mainCategoryId) {
        if (mainCategoryId == null) {
            return null;
        }

        return qPetTalk.mainCategory.id.eq(mainCategoryId);
    }

    private BooleanExpression eqSubCategory(Long subCategoryId) {
        if (subCategoryId == null) {
            return null;
        }

        return qPetTalk.subCategory.id.eq(subCategoryId);
    }
}
