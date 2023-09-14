package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalkReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetTalkReplyRepository extends JpaRepository<PetTalkReply, Long> {
    List<PetTalkReply> findAllByPetTalkId(Long petTalkId);
}