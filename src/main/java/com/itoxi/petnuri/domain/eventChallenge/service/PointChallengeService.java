package com.itoxi.petnuri.domain.eventChallenge.service;

import com.itoxi.petnuri.domain.eventChallenge.dto.request.WritePointChallengePostRequest;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.repository.PointChallengeRepository;
import lombok.RequiredArgsConstructor;
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
    public PointChallenge loadPointChallengePostDetails(Long pointChallengeId) {
        return pointChallengeRepository.loadPointChallengePostDetails(pointChallengeId);
    }

}
