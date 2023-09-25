package com.itoxi.petnuri.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPhoto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import com.itoxi.petnuri.global.common.exception.Exception500;
import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private static final String DATE_FORMAT = "yyyy/MM/dd/";
    private static final String PET_TALK_PHOTO_PREFIX = "pet_talk/";
    private static final String MEMBER_IMAGE = "profile_image/";

    private static final String DAILY_CHALLENGE_IMAGE_PREFIX = "daily_challenge/";

    private static final String PET_IMAGE_PREFIX = "pet_image/";

    @Transactional
    public List<PetTalkPhoto> uploadPetTalkPhotos(MultipartFile[] files, PetTalk petTalk) {
        List<PetTalkPhoto> petTalkPhotos = new ArrayList<>();

        try {
            log.info("[펫톡] 이미지 업로드 진행");
            for (MultipartFile file : files) {
                String fileKey = PET_TALK_PHOTO_PREFIX + createDatePath() + generateRandomFileName();
                ObjectMetadata metadata = createObjectMetadataFromFile(file);
                amazonS3.putObject(bucket, fileKey, file.getInputStream(), metadata);

                petTalkPhotos.add(PetTalkPhoto.builder()
                        .name(file.getOriginalFilename())
                        .url(getUrlFromBucket(fileKey))
                        .petTalk(petTalk)
                        .build());
            }

        } catch (Exception e) {
            log.error("[펫톡] 이미지 업로드 중 오류 발생 : " + e.getMessage());
        }

        log.info("[펫톡] 이미지 업로드 완료");
        return petTalkPhotos;
    }

    // 프로필 이미지 수정
    public String uploadProfileImage(MultipartFile file) {
        String profileImageUrl = uploadImage(MEMBER_IMAGE, file);
        return getUrlFromBucket(profileImageUrl);
    }

    //펫 프로필 이미지 저장
    public String uploadPetProfileImage(MultipartFile image){
        String petProfileImageUrl = uploadImage(PET_IMAGE_PREFIX, image);
        return getUrlFromBucket(petProfileImageUrl);
    }

    // 단일 파일 저장
    private String uploadImage(String subject, MultipartFile file) {
        String fileKey = subject + createDatePath() + generateRandomFileName();
        ObjectMetadata metadata = createObjectMetadataFromFile(file);
        try {
            amazonS3.putObject(bucket, fileKey, file.getInputStream(), metadata);
        } catch (Exception e) {
            throw new Exception500(ErrorCode.S3UPLOADER_ERROR);
        }
        return getUrlFromBucket(fileKey);
    }

    private String generateRandomFileName() {
        return UUID.randomUUID().toString();
    }

    private String createDatePath() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return now.format(dateTimeFormatter);
    }

    private ObjectMetadata createObjectMetadataFromFile(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    private String getUrlFromBucket(String fileKey) {
        return amazonS3.getUrl(bucket, fileKey).toString();
    }

    private final Function<String, String> getPrefix =
            prefix -> DAILY_CHALLENGE_IMAGE_PREFIX + createDatePath() + generateRandomFileName();

    // S3 deleteObject()의 filename 생성용
    private final Function<String, String> getFileNameFromUrl = url -> {
        // 1. amazonaws.com 이 위치한 문자열 index를 가져온다.
        int startIndex = url.indexOf(".com");

        // 2. startIndex에서부터 처음 나오는 "/"의 위치 다움 문자 인덱스를 가져 온다.
        int fromIndex = url.indexOf("/", startIndex) + 1;

        // 3. fromIndex부터 끝까지 문자열을 잘라서 filename을 만들어서 반환.
        return url.substring(fromIndex, url.length());
    };

    public String uploadDailyChallengeImage(MultipartFile file) {
        String fileKey = getPrefix.apply(DAILY_CHALLENGE_IMAGE_PREFIX);
        ObjectMetadata metadata = createObjectMetadataFromFile(file);
        try {
            amazonS3.putObject(bucket, fileKey, file.getInputStream(), metadata);
        } catch (Exception e) {
            throw new Exception500(ErrorCode.FILE_TRANSFER_ERROR);
        }
        return getUrlFromBucket(fileKey);
    }
}
