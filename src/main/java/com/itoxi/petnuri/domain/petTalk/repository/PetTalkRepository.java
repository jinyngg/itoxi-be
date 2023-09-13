package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;

public interface PetTalkRepository {

    PetTalk getById(Long petTalkId);

    void write(PetTalk petTalk);

}
