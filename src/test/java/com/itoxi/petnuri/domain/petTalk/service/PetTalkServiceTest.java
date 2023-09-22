package com.itoxi.petnuri.domain.petTalk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkRequest;
import com.itoxi.petnuri.domain.petTalk.entity.MainCategory;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.SubCategory;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import com.itoxi.petnuri.domain.petTalk.type.OrderType;
import com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class PetTalkServiceTest {

    @Mock
    private PrincipalDetails principalDetails;

    @Mock
    private Authentication authentication;

    @Mock
    private PetTalkRepository petTalkRepository;

    @InjectMocks
    private PetTalkService petTalkService;

    @InjectMocks
    private AmazonS3Service amazonS3Service;


    @Test
    @DisplayName("펫톡 게시글 작성(이미지 없음) - 성공")
    void write_success() {

        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .id(memberId)
                .nickname("테스터")
                .profileImageUrl("")
                .build();

        Long mainCategoryId = 1L;
        MainCategory mainCategory = buildMainCategory(mainCategoryId);

        Long subCategoryId = 1L;
        SubCategory subCategory = buildSubCategory(subCategoryId, mainCategory);

        given(petTalkRepository.getMainCategoryById(anyLong()))
                .willReturn(mainCategory);

        given(petTalkRepository.getSubCategoryById(anyLong()))
                .willReturn(subCategory);

        MultipartFile[] files = null;

        WritePetTalkRequest request = WritePetTalkRequest.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .mainCategoryId(mainCategoryId)
                .subCategoryId(subCategoryId)
                .petType(PetType.DOG)
                .build();

        // when
        when(principalDetails.getMember()).thenReturn(member);
        petTalkService.write(principalDetails, files, request);

        // then
        verify(petTalkRepository, times(1)).write(any(PetTalk.class));
        verify(petTalkRepository, times(0)).uploadPetTalkPhotos(any(MultipartFile[].class), any(PetTalk.class));
    }

    @Test
    @DisplayName("펫톡 게시글 목록 최신순 조회(비회원) - 성공")
    void loadPetTalkPosts_success() {

        // given
        Long mainCategoryId = 1L;
        Long subCategoryId = 1L;
        PetType petType = PetType.DOG;
        OrderType order = OrderType.LATEST;
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);

        MainCategory mainCategory = buildMainCategory(mainCategoryId);
        SubCategory subCategory = buildSubCategory(subCategoryId, mainCategory);

        Long memberId = 1L;
        Member writer = Member.builder()
                .id(memberId)
                .nickname("글 작성자")
                .profileImageUrl("")
                .build();

        PetTalk petTalk1 = PetTalk.builder()
                .title("제목 1")
                .content("내용 1")
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .petType(PetType.DOG)
                .status(PetTalkStatus.ACTIVE)
                .writer(writer)
                .build();

        PetTalk petTalk2 = PetTalk.builder()
                .title("제목 2")
                .content("내용 2")
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .petType(PetType.DOG)
                .status(PetTalkStatus.ACTIVE)
                .writer(writer)
                .build();

        List<PetTalk> dogs = Arrays.asList(petTalk1, petTalk2);
        Page<PetTalk> petTalks =
                PageableExecutionUtils.getPage(dogs, pageable, () -> dogs.size());

        // when
        when(authentication.getPrincipal()).thenReturn(principalDetails);
        when(principalDetails.getMember()).thenReturn(null);

        when(petTalkRepository.loadLatestPetTalkPostsByCategoryAndPetType(page, size, mainCategoryId, subCategoryId, petType))
                .thenReturn(petTalks);

        Page<PetTalk> repositoryResult =
                petTalkRepository.loadLatestPetTalkPostsByCategoryAndPetType(page, size, mainCategoryId, subCategoryId, petType);
        Page<PetTalk> serviceResult = petTalkService.loadPetTalkPosts(authentication, mainCategoryId, subCategoryId, petType, order, page, size);

        // then
        assertEquals(repositoryResult.getContent().size(), serviceResult.getContent().size());

    }

    private MainCategory buildMainCategory(Long mainCategoryId) {
        return MainCategory.builder()
                .id(mainCategoryId)
                .name("자유수다")
                .build();
    }

    private SubCategory buildSubCategory(Long subCategoryId, MainCategory mainCategory) {
        return SubCategory.builder()
                .id(subCategoryId)
                .name("질병/관리")
                .mainCategory(mainCategory)
                .build();
    }
}