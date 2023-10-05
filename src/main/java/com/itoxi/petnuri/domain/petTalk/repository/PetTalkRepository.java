package com.itoxi.petnuri.domain.petTalk.repository;

import static com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus.DELETED;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.INVALID_MAIN_CATEGORY_ID;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.INVALID_PET_TALK_ID;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.INVALID_SUB_CATEGORY_ID;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.domain.petTalk.entity.MainCategory;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPhoto;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkView;
import com.itoxi.petnuri.domain.petTalk.entity.SubCategory;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@RequiredArgsConstructor
public class PetTalkRepository {

    private final AmazonS3Service amazonS3Service;
    private final PetTalkJpaRepository petTalkJpaRepository;
    private final PetTalkPhotoJpaRepository petTalkPhotoJpaRepository;
    private final PetTalkEmotionRepository petTalkEmotionRepository;
    private final MainCategoryJpaRepository mainCategoryJpaRepository;
    private final SubCategoryJpaRepository subCategoryJpaRepository;
    private final MemberRepository memberRepository;

    public PetTalk getById(Long petTalkId) {
        return petTalkJpaRepository.findById(petTalkId)
                .orElseThrow(() -> new Exception400(INVALID_PET_TALK_ID));
    }

    public void write(PetTalk petTalk) {
        petTalkJpaRepository.save(petTalk);
    }

    public void uploadPetTalkPhotos(MultipartFile[] files, PetTalk petTalk) {
        List<PetTalkPhoto> photos = amazonS3Service.uploadPetTalkPhotos(files, petTalk);

        if (!photos.isEmpty()) {
            String thumbnail = photos.get(0).getUrl();
            petTalk.uploadThumbnail(thumbnail);
        }

        petTalk.uploadPetTalkPhotos(photos);
    }

    public void uploadPetTalkPhoto(MultipartFile files, PetTalk petTalk) {
        List<PetTalkPhoto> photos = amazonS3Service.uploadPetTalkPhoto(files, petTalk);

        if (!photos.isEmpty()) {
            String thumbnail = photos.get(0).getUrl();
            petTalk.uploadThumbnail(thumbnail);
        }

        petTalk.uploadPetTalkPhotos(photos);
    }

    public Page<PetTalkView> loadLatestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        return petTalkJpaRepository.loadLatestPetTalkViewsByCategoryAndPetType(
                page, size, mainCategoryId, subCategoryId, petType);
    }

    public Page<PetTalkView> loadBestPetTalkViewsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        return petTalkJpaRepository.loadBestPetTalkViewsByCategoryAndPetType(
                page, size, mainCategoryId, subCategoryId, petType);
    }

    public void updateWriterAndPhotosToPetTalkView(PetTalkView petTalkView) {
        memberRepository.findById(petTalkView.getMemberId()).ifPresent(petTalkView::updateWriter);
        List<PetTalkPhoto> petTalkPhotos = petTalkPhotoJpaRepository.findAllByPetTalkIdOrderByIdAsc(petTalkView.getPetTalkId());
        if (!petTalkPhotos.isEmpty()) {
            petTalkView.uploadThumbnail(petTalkPhotos.get(0).getUrl());
            petTalkView.uploadPetTalkPhotos(petTalkPhotos);
        }

    }

    public PetTalkView loadPetTalkPostsDetails(Long petTalkId) {
        return petTalkJpaRepository.loadPetTalkPostsDetails(petTalkId)
                .orElseThrow(() -> new Exception400(INVALID_PET_TALK_ID));
    }

    public void updateReactedStatusByMemberAndPetTalkViews(Page<PetTalkView> petTalkViews, Member member) {
        for (PetTalkView petTalkView : petTalkViews) {
            boolean reacted =
                    petTalkEmotionRepository.existsByMemberIdAndPetTalkId(member.getId(), petTalkView.getPetTalkId());
            petTalkView.react(reacted);
        }
    }

    public void updateReactedStatusByMemberAndPetTalk(PetTalkView petTalkView, Member member) {
        boolean reacted =
                petTalkEmotionRepository.existsByMemberIdAndPetTalkId(member.getId(), petTalkView.getPetTalkId());
        petTalkView.react(reacted);
    }

    public void addEmojiCount(PetTalk petTalk) {
        petTalkJpaRepository.save(petTalk.addEmojiCount());
    }

    public void subtractEmojiCount(PetTalk petTalk) {
        petTalkJpaRepository.save(petTalk.subtractEmojiCount());
    }

    public void addReplyCount(PetTalk petTalk) {
        petTalkJpaRepository.save(petTalk.addReplyCount());
    }

    public void updateViewCount(Long petTalkId, Long petTalkViewCount) {
        petTalkJpaRepository.addViewCountFromRedis(petTalkId, petTalkViewCount);
    }

    public void deletePetTalkPost(PetTalk petTalk) {
        petTalk.updateStatus(DELETED);
        petTalkJpaRepository.save(petTalk);
    }

    public MainCategory getMainCategoryById(Long mainCategoryId) {
        return mainCategoryJpaRepository.findById(mainCategoryId)
                .orElseThrow(() -> new Exception400(INVALID_MAIN_CATEGORY_ID));
    }

    public SubCategory getSubCategoryById(Long subCategoryId) {
        return subCategoryJpaRepository.findById(subCategoryId)
                .orElseThrow(() -> new Exception400(INVALID_SUB_CATEGORY_ID));
    }

    public List<PetTalk> findTopPetTalksOrderByRanking(LocalDateTime oneWeekAgo, Pageable pageable) {
        return petTalkJpaRepository.findTopPetTalksOrderByRanking(oneWeekAgo, pageable);
    }

    public List<PetTalk> findAllByWriter(Member member) {
        return petTalkJpaRepository.findAllByWriter(member);
    }

    public void deleteAll(List<PetTalk> petTalkList) {
        petTalkJpaRepository.deleteAll(petTalkList);
    }
}
