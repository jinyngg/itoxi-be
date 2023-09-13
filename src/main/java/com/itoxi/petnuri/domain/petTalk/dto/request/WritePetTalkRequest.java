package com.itoxi.petnuri.domain.petTalk.dto.request;

import com.itoxi.petnuri.domain.petTalk.type.MainCategory;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.domain.petTalk.type.SubCategory;
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
