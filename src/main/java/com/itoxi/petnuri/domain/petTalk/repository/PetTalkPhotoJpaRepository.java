package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetTalkPhotoJpaRepository extends JpaRepository<PetTalkPhoto, Long> {
    List<PetTalkPhoto> findAllByPetTalkIdOrderByIdAsc(Long petTalkId);
}
