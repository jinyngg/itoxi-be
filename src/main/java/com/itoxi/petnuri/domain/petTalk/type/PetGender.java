package com.itoxi.petnuri.domain.petTalk.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PetGender {
    MALE("남"), FEMALE("여");

    private final String label;

    public static PetGender stringToPetGender(String petGender){
        if(petGender.equals("남")){
            return PetGender.MALE;
        }else if(petGender.equals("여")){
            return PetGender.FEMALE;
        }

        return null;

    }
}
