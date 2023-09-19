package com.itoxi.petnuri.domain.petTalk.repository.impl;

import static com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus.ACTIVE;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.QPetTalk;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkJpaRepositoryCustom;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PetTalkJpaRepositoryCustomImpl implements PetTalkJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QPetTalk qPetTalk = QPetTalk.petTalk;

    @Override
    public Page<PetTalk> loadLatestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        Pageable pageable = PageRequest.of(page, size);

        List<PetTalk> petTalks = queryFactory
                .selectFrom(qPetTalk)
                .where(isStatusActive()
                        .and(qPetTalk.mainCategory.id.eq(mainCategoryId))
                        .and(qPetTalk.petType.eq(petType)))
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

        List<PetTalk> petTalks = queryFactory
                .selectFrom(qPetTalk)
                .where(isStatusActive()
                        .and(qPetTalk.createdAt.after(LocalDateTime.now().minusDays(3)))
                        .and(qPetTalk.petType.eq(petType)))
                .orderBy(
                        qPetTalk.viewCount.desc(),
                        qPetTalk.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = petTalks.size();

        return PageableExecutionUtils.getPage(petTalks, pageable, () -> totalCount);
    }

    private BooleanExpression isStatusActive() {
        return qPetTalk.status.eq(ACTIVE);
    }
}
