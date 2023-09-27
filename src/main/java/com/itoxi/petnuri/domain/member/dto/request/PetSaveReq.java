package com.itoxi.petnuri.domain.member.dto.request;

import com.itoxi.petnuri.domain.petTalk.type.PetGender;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.common.customValid.valid.ValidEnum;
import com.itoxi.petnuri.global.common.customValid.valid.ValidPetName;
import lombok.Getter;

@Getter
public class PetSaveReq {
    private PetType species;

    @ValidPetName
    private String petName;

    private String breed;

    private String petGender;

    private int petAge;


}