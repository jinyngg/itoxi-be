package com.itoxi.petnuri.domain.petTalk.repository.impl;

import static com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus.ACTIVE;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPost;
import com.itoxi.petnuri.domain.petTalk.entity.QPetTalkPost;
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

    QPetTalkPost qPetTalkPost = QPetTalkPost.petTalkPost;

    @Override
    public Page<PetTalkPost> loadLatestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        Pageable pageable = PageRequest.of(page, size);

        List<PetTalkPost> petTalkPosts = queryFactory
                .selectFrom(qPetTalkPost)
                .where(isStatusActive()
                        .and(qPetTalkPost.mainCategory.id.eq(mainCategoryId))
                        .and(qPetTalkPost.petType.eq(petType)))
                .orderBy(qPetTalkPost.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = petTalkPosts.size();

        return PageableExecutionUtils.getPage(petTalkPosts, pageable, () -> totalCount);
    }

    @Override
    public Page<PetTalkPost> loadBestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        Pageable pageable = PageRequest.of(page, size);

        List<PetTalkPost> petTalkPosts = queryFactory
                .selectFrom(qPetTalkPost)
                .where(isStatusActive()
                        .and(qPetTalkPost.createdAt.after(LocalDateTime.now().minusDays(3)))
                        .and(qPetTalkPost.petType.eq(petType)))
                // 조회수 > TODO 좋아요 > 댓글
                .orderBy(
                        qPetTalkPost.viewCount.desc(),
                        qPetTalkPost.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = petTalkPosts.size();

        return PageableExecutionUtils.getPage(petTalkPosts, pageable, () -> totalCount);
    }

    private BooleanExpression isStatusActive() {
        return qPetTalkPost.status.eq(ACTIVE);
    }
}
