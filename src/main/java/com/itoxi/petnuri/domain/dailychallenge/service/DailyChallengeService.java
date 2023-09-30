package com.itoxi.petnuri.domain.dailychallenge.service;

import com.itoxi.petnuri.domain.dailychallenge.dto.request.DailyChallengeRequest;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyAuthImageResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeDetailResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyChallengeRepository;
import com.itoxi.petnuri.domain.dailychallenge.type.ChallengeStatus;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_DAILY_CHALLENGE_ID;

/**
 * author         : Jisang Lee
 * date           : 2023-09-25
 * description    :
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyChallengeService {

    private final DailyChallengeRepository dailyChallengeRepository;
    private final AmazonS3Service amazonS3Service;

    public List<DailyChallengeListResponse> respDailyChallengeList(Member loginMember) {
        // 반환에 필요한 데이터 : 챌린지 id, 챌리지명, 로그인한 유저의 참여 여부, 썸네일 url
        return dailyChallengeRepository.findAllWithAuthStatus(loginMember);
    }

    public Page<DailyAuthImageResponse> respDailyChallengeAuthList(
            Long dailyChallengeId, Pageable pageable) {

        isValidChallengeId(dailyChallengeId); // 챌린지 id 검증
        return dailyChallengeRepository.findAllAuthList(dailyChallengeId, pageable);
    }

    public DailyChallengeDetailResponse respDailyChallengeDetail(
            Long dailyChallengeId, Member loginMember) {

        isValidChallengeId(dailyChallengeId);
        return dailyChallengeRepository.findDetailChallenge(dailyChallengeId, loginMember);
    }

    @Transactional
    public void registerDailyChallenge(
            DailyChallengeRequest request, MultipartFile thumbnail, MultipartFile banner
    ) {
        String thumbnailUrl = amazonS3Service.uploadThumbnailImage(thumbnail);
        String bannerUrl = amazonS3Service.uploadBannerImage(banner);
        DailyChallenge dailyChallenge = DailyChallenge.toEntity(request, thumbnailUrl, bannerUrl);
        dailyChallengeRepository.save(dailyChallenge);
    }

    private void isValidChallengeId(Long challengeId) {
        if (!dailyChallengeRepository.existsByIdAndChallengeStatus(
                challengeId, ChallengeStatus.OPENED)) {
            throw new Exception400(NOT_FOUND_DAILY_CHALLENGE_ID);
        }
    }

}

