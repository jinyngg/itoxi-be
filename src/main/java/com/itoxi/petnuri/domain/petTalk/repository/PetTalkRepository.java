package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPhoto;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@RequiredArgsConstructor
public class PetTalkRepository {

    private final AmazonS3Service amazonS3Service;
    private final PetTalkJpaRepository petTalkJpaRepository;

    public PetTalk getById(Long petTalkId) {
        return petTalkJpaRepository.findById(petTalkId)
                .orElseThrow(() -> new RuntimeException("펫톡커스텀"));
    }

    public PetTalk write(PetTalk petTalk) {
        return petTalkJpaRepository.save(petTalk);
    }

    public void uploadPetTalkPhotos(MultipartFile[] files, PetTalk petTalk) {
        List<PetTalkPhoto> photos = amazonS3Service.uploadPetTalkPhotos(files, petTalk);

        if (!photos.isEmpty()) {
            String thumbnail = photos.get(0).getUrl();
            petTalk.uploadThumbnail(thumbnail);
        }

        petTalk.uploadPetTalkPhotos(photos);
        petTalkJpaRepository.save(petTalk);
    }

    public Page<PetTalk> loadLatestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        return petTalkJpaRepository.loadLatestPetTalkPostsByCategoryAndPetType(
                page, size, mainCategoryId, subCategoryId, petType);
    }

    public Page<PetTalk> loadBestPetTalkPostsByCategoryAndPetType(
            int page, int size, Long mainCategoryId, Long subCategoryId, PetType petType) {
        return petTalkJpaRepository.loadBestPetTalkPostsByCategoryAndPetType(
                page, size, mainCategoryId, subCategoryId, petType);
    }

}
