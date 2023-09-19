package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTalkJpaRepository extends JpaRepository<PetTalkPost, Long>, PetTalkJpaRepositoryCustom {

}
