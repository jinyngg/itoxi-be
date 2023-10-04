package com.itoxi.petnuri.domain.point.service;

import static com.itoxi.petnuri.domain.point.entity.Point.*;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_MEMBER_ID;

import com.itoxi.petnuri.domain.dailychallenge.dto.DailyAuthDto;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.domain.point.dto.response.PointResponse;
import com.itoxi.petnuri.domain.point.entity.Point;
import com.itoxi.petnuri.domain.point.entity.PointHistory;
import com.itoxi.petnuri.domain.point.repository.PointHistoryRepository;
import com.itoxi.petnuri.domain.point.repository.PointRepository;
import com.itoxi.petnuri.global.common.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MemberRepository memberRepository;

    public Long getPoint(DailyAuthDto dailyAuthDto, Long userId) {

        // 1. 회원 id로 회원 정보를 가져 온다.
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new Exception400(NOT_FOUND_MEMBER_ID));

        // 3. 회원의 포인트 정보와 이력을 업데이트.
        Point point = pointRepository.findByMember(member)
                .orElseGet(() -> createPoint(member)); // 회원의 포인트 정보가 null이면 신규 생성.

        PointHistory pointHistory = PointHistory.createGetPointHistory(
                point, dailyAuthDto.getPayment(), dailyAuthDto.getChallengeTitle());
        pointHistoryRepository.save(pointHistory);

        return point.getHavePoint();
    }

    public PointResponse getPointResponse(Long memberId) {
        isValidMemberId(memberId);
        return pointRepository.findPointByMemberId(memberId);
    }

    public void isValidMemberId(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new Exception400(NOT_FOUND_MEMBER_ID);
        }
    }

}
