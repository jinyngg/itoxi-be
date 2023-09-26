package com.itoxi.petnuri.domain.eventChallenge.service;

import com.itoxi.petnuri.domain.eventChallenge.dto.request.WritePointChallengeReviewRequest;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReview;
import com.itoxi.petnuri.domain.eventChallenge.repository.PointChallengeRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PointChallengeReviewService {

    private final PointChallengeRepository pointChallengeRepository;

    // TODO
    private final MemberRepository memberRepository;

    @Transactional
    public void write(
            Long pointChallengeId,
            MultipartFile file,
            WritePointChallengeReviewRequest request,
            PrincipalDetails principalDetails) {
        // TODO 1. 로그인된 회원 정보 확인
//        Member reviewer = principalDetails.getMember();
        Member reviewer = memberRepository.findById(1L).orElseThrow();

        // 2. 챌린지 조회
        PointChallenge pointChallenge =
                pointChallengeRepository.getPointChallengeById(pointChallengeId);

        // 3. 리뷰 생성
        PointChallengeReview pointChallengeReview = PointChallengeReview.builder()
                // TODO
                .reviewer(reviewer)
                .pointChallenge(pointChallenge)
                .photoName(file.getName())
                .content(request.getContent())
                .build();
        
        // 4. 리뷰 사진 업로드
        pointChallengeRepository.uploadPointChallengeReviewPhoto(file, pointChallengeReview);
        
        // 5. 리뷰 저장
        pointChallengeRepository.writePointChallengeReview(pointChallengeReview);
    }

    @Transactional(readOnly = true)
    public List<PointChallengeReview> loadMyReviewsOfThePointChallenge(
            Authentication authentication, Long pointChallengeId) {
        // 1. 로그인이 되어있지 않을 경우, 빈 리스트 리턴
        if (authentication == null) {
            return new ArrayList<>();
        }

        // 2. 로그인된 유저 조회
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member challenger = principalDetails.getMember();

        // 3. 챌린지 조회
        PointChallenge pointChallenge =
                pointChallengeRepository.getPointChallengeById(pointChallengeId);

        // 4. 로그인된 유저의 챌린지 리뷰 조회
        return pointChallengeRepository.loadMyReviewsOfThePointChallenge(
                challenger, pointChallenge);
    }

}
