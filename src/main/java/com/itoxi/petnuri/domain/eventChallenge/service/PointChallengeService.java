package com.itoxi.petnuri.domain.eventChallenge.service;

import com.itoxi.petnuri.domain.eventChallenge.dto.request.WritePointChallengePostRequest;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.repository.PointChallengeRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PointChallengeService {

    private final PointChallengeRepository pointChallengeRepository;

    @Transactional
    public void write(
            MultipartFile thumbnail, MultipartFile poster,
            WritePointChallengePostRequest request) {
        // 1. 챌린지 생성
        PointChallenge pointChallenge = PointChallenge.builder()
                .title(request.getTitle())
                .subTitle(request.getSubTitle())
                .notice(request.getNotice())
                .point(request.getPoint())
                .pointMethod(request.getPointMethod())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        // 2. 썸네일 및 포스터 업데이트
        pointChallengeRepository.uploadPointChallengeThumbnail(thumbnail, pointChallenge);
        pointChallengeRepository.uploadPointChallengePoster(poster, pointChallenge);

        // 3. 챌린지 저장
        pointChallengeRepository.writePointChallengePost(pointChallenge);
    }

    @Transactional(readOnly = true)
    public PointChallenge loadPointChallengePostDetails(Authentication authentication, Long pointChallengeId) {
        // 1. 로그인된 회원 정보 확인
        Member member = null;
        if (authentication != null) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            member = principalDetails.getMember();
        }

        boolean isNonMember = (member == null);

        PointChallenge pointChallenge =
                pointChallengeRepository.loadPointChallengePostDetails(pointChallengeId);
        if (isNonMember) {
            return pointChallenge;
        }

        pointChallengeRepository.updateWrittenReviewToday(pointChallenge, member);
        return pointChallenge;
    }

}
