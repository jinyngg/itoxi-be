package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PetTalkRepositoryAdapter implements PetTalkRepository {

    private final PetTalkJpaRepository petTalkJpaRepository;

    @Override
    public PetTalk getById(Long petTalkId) {
        return petTalkJpaRepository.findById(petTalkId)
                .orElseThrow(() -> new RuntimeException("펫톡커스텀"));
    }

    @Override
    public void write(PetTalk petTalk) {
        petTalkJpaRepository.save(petTalk);
    }

}
