package com.itoxi.petnuri.domain.member.dto.request;

import com.itoxi.petnuri.domain.petTalk.type.PetGender;
import lombok.Getter;

@Getter
public class PetProfileReq {
    private Long petId;
    private String petName;
    private PetGender petGender;
    private int petAge;
    private Boolean isSelected;
}
