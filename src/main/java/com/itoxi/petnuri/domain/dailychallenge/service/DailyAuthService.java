package com.itoxi.petnuri.domain.dailychallenge.service;

import com.itoxi.petnuri.domain.dailychallenge.dto.DailyAuthDto;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyAuth;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyAuthRepository;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyChallengeRepository;
import com.itoxi.petnuri.domain.dailychallenge.type.ChallengeStatus;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_DAILY_CHALLENGE_ID;

/**
 * author         : matrix
 * date           : 2023-09-18
 * description    :
 */
@Service
@RequiredArgsConstructor
public class DailyAuthService {

    private final DailyAuthRepository dailyAuthRepository;
    private final DailyChallengeRepository dailyChallengeRepository;
    private final AmazonS3Service amazonS3Service;

    // 인증글 작성(인증 사진 업로드)
    @Transactional
    public DailyAuthDto saveAuth(
            Member loginMember, Long dailyChallengeId, MultipartFile file) {

        // 1. 챌린지 id 검증
        DailyChallenge dailyChallenge = dailyChallengeRepository
                .findByIdAndChallengeStatus(dailyChallengeId, ChallengeStatus.OPENED)
                .orElseThrow(() -> new Exception400(NOT_FOUND_DAILY_CHALLENGE_ID));

        // 2. 이미 인증글을 작성했다면 예외 던지기
        isDupeAuthMember(loginMember.getId(), dailyChallengeId);

        // 3. 인증사진을 AWS S3에 저장
        String awsUrl = amazonS3Service.uploadDailyChallengeAuthImage(file);

        // 4. 인증글 저장
        // 필요 데이터 : 1) 인증 사진 url, 2) 데일리 챌린지 엔티티 , 3) 유저 엔티티
        DailyAuth dailyAuth = DailyAuth.toEntity(loginMember, dailyChallenge, awsUrl);
        dailyAuthRepository.save(dailyAuth);

        // 5. 회원의 포인트 적립을 위한 정보를 담아서 리턴
        return DailyAuthDto.of(dailyAuth, dailyChallenge);
    }

    private void isDupeAuthMember(Long loginMemberId, Long dailyChallengeId) {
        // Todo: 날짜 체크를 우선 query에서 비교하도록 구현하였음.
        if (dailyAuthRepository.dupeAuthCheck(loginMemberId, dailyChallengeId)) {
            throw new Exception400(ErrorCode.DUPE_POST_MEMBER);
        }
    }

}
