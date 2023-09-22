package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkEmotion;
import com.itoxi.petnuri.domain.petTalk.type.EmojiType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetTalkEmotionRepository extends JpaRepository<PetTalkEmotion, Long> {

    boolean existsByMemberAndPetTalkAndEmoji(Member member, PetTalk petTalk, EmojiType emoji);

    Optional<PetTalkEmotion> findByMemberAndPetTalkAndEmoji(Member member, PetTalk petTalk, EmojiType emoji);

    boolean existsByMemberIdAndPetTalkId(Long memberId, Long PetTalkId);
}