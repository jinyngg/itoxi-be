package com.itoxi.petnuri.domain.eventChallenge.repository;

import static com.itoxi.petnuri.domain.eventChallenge.type.EventChallengeType.POINT;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_EVENT_CHALLENGE_ID;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_MEMBER_REVIEW;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReview;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReward;
import com.itoxi.petnuri.domain.eventChallenge.type.PointChallengeStatus;
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
    private final PointChallengeJpaRepository pointChallengeJpaRepository;
    private final PointChallengeReviewJpaRepository pointChallengeReviewJpaRepository;
    private final PointChallengeRewardJpaRepository pointChallengeRewardJpaRepository;

    public PointChallenge getPointChallengeById(Long pointChallengeId) {
        return pointChallengeJpaRepository.findById(pointChallengeId)
                .orElseThrow(() -> new Exception400(NOT_FOUND_EVENT_CHALLENGE_ID));
    }

    public void writePointChallengePost(PointChallenge pointChallenge) {
        pointChallengeJpaRepository.save(pointChallenge);
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

    public boolean isWritePointChallengeReviewToday(PointChallenge pointChallenge, Member member) {
        return pointChallengeReviewJpaRepository.existsTodayReviewByPointChallengeAndReviewer(
                pointChallenge, member);
    }

    public PointChallengeReview getLatestPointChallengeReviewByPointChallengeAndMember(
            PointChallenge pointChallenge, Member member) {
        return pointChallengeReviewJpaRepository.findLatestPointChallengeReviewByPointChallengeAndMember(pointChallenge, member)
                .orElseThrow(() -> new Exception400(NOT_FOUND_MEMBER_REVIEW));
    }

    public List<PointChallenge> getPointChallengeByStatus(PointChallengeStatus status) {
        return pointChallengeJpaRepository.findAllByStatus(status);
    }

    public List<PointChallenge> getPointChallengeExceptClosed() {
        return pointChallengeJpaRepository.findAllExceptClosed();
    }

    public void updatePointChallenges(List<PointChallenge> pointChallenges) {
        if (!pointChallenges.isEmpty()) {
            pointChallengeJpaRepository.saveAll(pointChallenges);
        }
    }

    public List<Member> getMembersForPointReward(PointChallenge pointChallenge, Long reviewCount) {
        return pointChallengeReviewJpaRepository.findMembersForPointReward(
                pointChallenge, reviewCount);
    }

    public List<PointChallengeReward> getPointChallengesRewardByPointChallenge(
            PointChallenge pointChallenge) {
        return pointChallengeRewardJpaRepository.findAllByPointChallenge(pointChallenge);
    }

    public void updatePointChallengeRewards(List<PointChallengeReward> pointChallengeRewards) {
        if (!pointChallengeRewards.isEmpty()) {
            pointChallengeRewardJpaRepository.saveAll(pointChallengeRewards);
        }
    }
}
