package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkEmotion;
import com.itoxi.petnuri.domain.petTalk.type.EmojiType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTalkEmotionRepository extends JpaRepository<PetTalkEmotion, Long> {
    boolean existsByMemberAndPetTalkAndEmoji(Member member, PetTalk petTalk, EmojiType emoji);
}