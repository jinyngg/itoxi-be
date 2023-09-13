package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTalkJpaRepository extends JpaRepository<PetTalk, Long>, PetTalkJpaRepositoryCustom {

}
