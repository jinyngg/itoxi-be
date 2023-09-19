package com.itoxi.petnuri.domain.point.service;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyChallengeRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.domain.point.PointGetDto;
import com.itoxi.petnuri.domain.point.PointStatus;
import com.itoxi.petnuri.domain.point.entity.Point;
import com.itoxi.petnuri.domain.point.entity.PointHistory;
import com.itoxi.petnuri.domain.point.repository.PointHistoryRepository;
import com.itoxi.petnuri.domain.point.repository.PointRepository;
import com.itoxi.petnuri.global.exception.IdNotfoundException;
import com.itoxi.petnuri.global.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.function.Function;

/**
 * author         : matrix
 * date           : 2023-09-19
 * description    :
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final DailyChallengeRepository dailyChallengeRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;

    public Long getPoint(Long challengeId, Long userId) {
        // 1. 챌린지 id로 챌린지 이름과 인증 완료지 지급 포인트를 가져온다.
        DailyChallenge dailyChallenge = dailyChallengeRepository.findById(challengeId)
                .orElseThrow(() -> new IdNotfoundException("존재하지 않는 챌린지 ID 입니다."));
        PointGetDto pointGetDto = PointGetDto.from(dailyChallenge);

        // 2. 회원 id로 회원 정보를 가져 온다.
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IdNotfoundException("존재하지 않는 회원 ID 입니다."));

        // 3. 회원의 포인트 정보와 이력을 업데이트.
        Point memberPoint = pointRepository.findByMember(member)
                .orElseGet(() -> createPoint(member)); // 회원의 포인트 정보가 null이면 신규 생성.
        memberPoint.addPoint(pointGetDto.getPoint());

        PointHistory pointHistory = PointHistory.builder()
                .point(memberPoint)
                .pointStatus(PointStatus.GET)
                .getPoint(pointGetDto.getPoint())
                .getMethod(pointGetDto.getChallengeName())
                .amount(memberPoint.getTotalPoint())
                .build();
        pointHistoryRepository.save(pointHistory);

        return memberPoint.getTotalPoint();
    }

    private Point createPoint(Member member) {
        Point point = Point.builder()
                .member(member)
                .totalPoint(0L) // 회원 포인트 초기값 0.
                .build();
        pointRepository.save(point);
        return point;
    }
}
