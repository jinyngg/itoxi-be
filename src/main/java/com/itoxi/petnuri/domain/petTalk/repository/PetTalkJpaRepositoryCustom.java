package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkView;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PetTalkJpaRepositoryCustom {

    Page<PetTalkView> loadLatestPetTalkViewsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType);

    Page<PetTalkView> loadBestPetTalkViewsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType);

    Optional<PetTalkView> loadPetTalkPostsDetails(Long petTalkId);

    void addViewCountFromRedis(Long petTalkId, Long petTalkViewCount);

}
