package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPost;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import org.springframework.data.domain.Page;

public interface PetTalkJpaRepositoryCustom {

    Page<PetTalkPost> loadLatestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType);

    Page<PetTalkPost> loadBestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType);

}
