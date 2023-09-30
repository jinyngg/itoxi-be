package com.itoxi.petnuri.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.itoxi.petnuri.domain.eventChallenge.type.EventChallengeType;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPhoto;
import com.itoxi.petnuri.global.common.exception.Exception500;
import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.itoxi.petnuri.domain.eventChallenge.type.EventChallengeType.POINT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private static final String DATE_FORMAT = "yyyy/MM/dd/";

    private static final String PET_TALK_PHOTO_PREFIX = "pet_talk/";
    private static final String MEMBER_IMAGE_PREFIX = "profile_image/";
    private static final String DAILY_CHALLENGE_AUTH_IMAGE_PREFIX = "daily_challenge/";
    private static final String DAILY_CHALLENGE_THUMBNAIL_PREFIX = "daily_challenge/thumbnail/";
    private static final String DAILY_CHALLENGE_BANNER_PREFIX = "daily_challenge/banner/";
    private static final String PET_IMAGE_PREFIX = "pet_image/";

    private static final String EVENT_CHALLENGE_POINT_THUMBNAIL_PREFIX =
            "event_challenge/point/thumbnail/";
    private static final String EVENT_CHALLENGE_REWARD_THUMBNAIL_PREFIX =
            "event_challenge/reward/thumbnail/";

    private static final String EVENT_CHALLENGE_POINT_POSTER_PREFIX =
            "event_challenge/point/poster/";
    private static final String EVENT_CHALLENGE_REWARD_POSTER_PREFIX =
            "event_challenge/reward/poster/";

    private static final String EVENT_CHALLENGE_POINT_REVIEW_PREFIX =
            "event_challenge/point/review/";
    private static final String EVENT_CHALLENGE_REWARD_REVIEW_PREFIX =
            "event_challenge/reward/review/";

    @Transactional
    public List<PetTalkPhoto> uploadPetTalkPhotos(MultipartFile[] files, PetTalk petTalk) {
        List<PetTalkPhoto> petTalkPhotos = new ArrayList<>();

        try {
            log.info("[펫톡] 이미지 업로드 진행");
            for (MultipartFile file : files) {
                petTalkPhotos.add(PetTalkPhoto.builder()
                        .name(file.getOriginalFilename())
                        .url(uploadImage(PET_TALK_PHOTO_PREFIX, file))
                        .petTalk(petTalk)
                        .build());
            }

        } catch (Exception e) {
            log.error("[펫톡] 이미지 업로드 중 오류 발생 : " + e.getMessage());
        }

        log.info("[펫톡] 이미지 업로드 완료");
        return petTalkPhotos;
    }

    @Transactional
    public String uploadRewardChallengeReviewImage(MultipartFile file) {
        return uploadImage(EVENT_CHALLENGE_REWARD_REVIEW_PREFIX, file);
    }

    public String uploadPointChallengeReviewPhoto(MultipartFile file) {
        return uploadImage(EVENT_CHALLENGE_POINT_REVIEW_PREFIX, file);
    }

    public String uploadEventChallengeThumbnail(EventChallengeType type, MultipartFile thumbnail) {
        String path = (type == POINT) ?
                EVENT_CHALLENGE_POINT_THUMBNAIL_PREFIX : EVENT_CHALLENGE_REWARD_THUMBNAIL_PREFIX;

        return uploadImage(path, thumbnail);
    }

    public String uploadEventChallengePoster(EventChallengeType type, MultipartFile poster) {
        String path = (type == POINT) ?
                EVENT_CHALLENGE_POINT_POSTER_PREFIX : EVENT_CHALLENGE_REWARD_POSTER_PREFIX;

        return uploadImage(path, poster);
    }

    // 프로필 이미지 수정
    public String uploadProfileImage(MultipartFile file) {
        return uploadImage(MEMBER_IMAGE_PREFIX, file);
    }

    //펫 프로필 이미지 저장
    public String uploadPetProfileImage(MultipartFile image){
        return uploadImage(PET_IMAGE_PREFIX, image);
    }

    // 데일리 챌린지 인증 사진 저장
    public String uploadDailyChallengeAuthImage(MultipartFile file) {
        return uploadImage(DAILY_CHALLENGE_AUTH_IMAGE_PREFIX, file);
    }

    // 데일리 챌린지 썸네일 저장
    public String uploadThumbnailImage(MultipartFile thumbnail) {
        return uploadImage(DAILY_CHALLENGE_THUMBNAIL_PREFIX, thumbnail);
    }

    // 데일리 챌린지 메인 배너 저장
    public String uploadBannerImage(MultipartFile banner) {
        return uploadImage(DAILY_CHALLENGE_BANNER_PREFIX, banner);
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

}
