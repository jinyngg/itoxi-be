package com.itoxi.petnuri.domain.petTalk.dto.request;

import com.itoxi.petnuri.domain.petTalk.type.PetType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WritePetTalkRequest {
    private PetType petType;
    private Long mainCategoryId;
    private Long subCategoryId;
    private String title;
    private String content;
}
