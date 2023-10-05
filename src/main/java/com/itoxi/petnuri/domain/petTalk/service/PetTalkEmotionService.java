package com.itoxi.petnuri.domain.petTalk.service;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.CreatePetTalkEmotionReq;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkEmotion;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkEmotionRepository;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import com.itoxi.petnuri.domain.petTalk.type.EmojiType;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.common.exception.Exception404;
import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetTalkEmotionService {
    private final PetTalkRepository petTalkRepository;
    private final PetTalkEmotionRepository petTalkEmotionRepository;

    @Transactional
    public void create(Member member, Long petTalkId, CreatePetTalkEmotionReq request) {
        PetTalk petTalk = petTalkRepository.getById(petTalkId);

        // 중복 레코드 검사
        boolean isExists = petTalkEmotionRepository.existsByMemberAndPetTalkAndEmoji(member,
                petTalk, request.getEmoji());
        if (isExists) {
            throw new Exception400(ErrorCode.DUPLICATED_DATA);
        }

        PetTalkEmotion petTalkEmotion = PetTalkEmotion.create(member, petTalk, request.getEmoji());
        petTalkRepository.addEmojiCount(petTalk);
        petTalkEmotionRepository.save(petTalkEmotion);
    }

    @Transactional
    public void delete(Member member, Long petTalkId, EmojiType emoji) {
        PetTalk petTalk = petTalkRepository.getById(petTalkId);

        PetTalkEmotion petTalkEmotion = petTalkEmotionRepository.findByMemberAndPetTalkAndEmoji(member,
                        petTalk, emoji)
                .orElseThrow(() -> new Exception400(ErrorCode.DATA_NOT_FOUND));

        petTalkRepository.subtractEmojiCount(petTalk);
        petTalkEmotionRepository.delete(petTalkEmotion);
    }
}