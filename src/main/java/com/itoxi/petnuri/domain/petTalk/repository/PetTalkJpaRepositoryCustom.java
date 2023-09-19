package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import org.springframework.data.domain.Page;

public interface PetTalkJpaRepositoryCustom {

    Page<PetTalk> loadLatestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType);

    Page<PetTalk> loadBestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType);

}
