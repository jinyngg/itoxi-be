package com.itoxi.petnuri.domain.petTalk.service.impl;

import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkRequest;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import com.itoxi.petnuri.domain.petTalk.service.PetTalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetTalkServiceImpl implements PetTalkService {

    private final PetTalkRepository petTalkRepository;

    @Transactional
    public void write(Authentication authentication, WritePetTalkRequest request) {
        // 1. 로그인된 회원 정보 확인

        // 2. 게시글 저장
        petTalkRepository.write(PetTalk.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .mainCategory(request.getMainCategory())
                .subCategory(request.getSubCategory())
                .petType(request.getPetType())
//                .writer(member)
                .build());
    }

}
