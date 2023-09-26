package com.itoxi.petnuri.domain.eventChallenge.repository;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_EVENT_CHALLENGE_ID;
import static com.itoxi.petnuri.domain.eventChallenge.type.EventChallengeType.POINT;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReview;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@RequiredArgsConstructor
public class PointChallengeRepository {

    private final AmazonS3Service amazonS3Service;
    private final PointChallengeJpaJpaRepository pointChallengeJPARepository;
    private final PointChallengeReviewReviewJpaRepository pointChallengeReviewJpaRepository;

    public PointChallenge getPointChallengeById(Long pointChallengeId) {
        return pointChallengeJPARepository.findById(pointChallengeId)
                .orElseThrow(() -> new Exception400(NOT_FOUND_EVENT_CHALLENGE_ID));
    }

    public void writePointChallengePost(PointChallenge pointChallenge) {
        pointChallengeJPARepository.save(pointChallenge);
    }

    public void writePointChallengeReview(PointChallengeReview pointChallengeReview) {
        pointChallengeReviewJpaRepository.save(pointChallengeReview);
    }

    public void uploadPointChallengeReviewPhoto(
            MultipartFile reviewPhoto, PointChallengeReview pointChallengeReview) {
        pointChallengeReview.uploadPhotoUrl(
                amazonS3Service.uploadPointChallengeReviewPhoto(reviewPhoto));
    }

    public void uploadPointChallengeThumbnail(
            MultipartFile thumbnail, PointChallenge pointChallenge) {
        String thumbnailUrl =
                amazonS3Service.uploadEventChallengeThumbnail(POINT, thumbnail);

        pointChallenge.uploadThumbnail(thumbnailUrl);
    }

    public void uploadPointChallengePoster(
            MultipartFile poster, PointChallenge pointChallenge) {
        String posterUrl =
                amazonS3Service.uploadEventChallengePoster(POINT, poster);

        pointChallenge.uploadPoster(posterUrl);
    }

    public PointChallenge loadPointChallengePostDetails(Long pointChallengeId) {
        return getPointChallengeById(pointChallengeId);
    }

    public List<PointChallengeReview> loadMyReviewsOfThePointChallenge(
            Member challenger, PointChallenge pointChallenge) {
        return pointChallengeReviewJpaRepository.findByPointChallengeAndReviewer(
                        pointChallenge, challenger);
    }

    public void uploadReviewPhoto(MultipartFile photo, PointChallengeReview pointChallengeReview) {

    }
}
