package com.itoxi.petnuri.domain.petTalk.service;

import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkRequest;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPost;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import com.itoxi.petnuri.domain.petTalk.type.OrderType;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PetTalkService {

    private final PetTalkRepository petTalkRepository;

    @Transactional
    public void write(
            Authentication authentication, MultipartFile[] files, WritePetTalkRequest request) {
        // 1. 로그인된 회원 정보 확인
        // TODO CustomUserDetails 관련 문의

        // 2. 게시글 저장
        PetTalkPost petTalkPost = petTalkRepository.write(PetTalkPost.builder()
                .title(request.getTitle())
                .content(request.getContent())
//                .mainCategory(request.getMainCategory())
//                .subCategory(request.getSubCategory())
                .petType(request.getPetType())
//                .writer(member)
                .build());

        // 3. 게시글 이미지 업로드
        petTalkRepository.uploadPetTalkPhotos(files, petTalkPost);
    }

    @Transactional(readOnly = true)
    public Page<PetTalkPost> loadPetTalkPosts(
            Long mainCategoryId, Long subCategoryId, PetType petType, OrderType order, int page, int size) {
        switch (order) {
            case BEST:
                return petTalkRepository.loadBestPetTalkPostsByCategoryAndPetType(
                        page, size, mainCategoryId, subCategoryId, petType);
            case LATEST:
            default:
                return petTalkRepository.loadLatestPetTalkPostsByCategoryAndPetType(
                        page, size, mainCategoryId, subCategoryId, petType);
        }
    }

}
