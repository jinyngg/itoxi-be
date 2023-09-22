package com.itoxi.petnuri.domain.petTalk.dto;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPhoto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetTalkPhotoDTO {

    private Long id;
    private String name;
    private String url;

    public static PetTalkPhotoDTO fromEntity(PetTalkPhoto petTalkPhoto) {
        return PetTalkPhotoDTO.builder()
                .id(petTalkPhoto.getId())
                .name(petTalkPhoto.getName())
                .url(petTalkPhoto.getUrl())
                .build();
    }

}
