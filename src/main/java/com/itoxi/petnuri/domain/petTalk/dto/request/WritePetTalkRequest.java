package com.itoxi.petnuri.domain.petTalk.dto.request;

import com.itoxi.petnuri.domain.petTalk.entity.MainCategory;
import com.itoxi.petnuri.domain.petTalk.entity.SubCategory;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WritePetTalkRequest {
    private PetType petType;
    private MainCategory mainCategory;
    private SubCategory subCategory;
    private String title;
    private String content;
}
