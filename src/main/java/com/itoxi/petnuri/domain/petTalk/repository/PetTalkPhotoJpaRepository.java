package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetTalkPhotoJpaRepository extends JpaRepository<PetTalkPhoto, Long> {
    List<PetTalkPhoto> findAllByPetTalkIdOrderByIdAsc(Long petTalkId);

    Optional<PetTalkPhoto> findTop1ByPetTalkOrderByIdAsc(PetTalk petTalk);
}
