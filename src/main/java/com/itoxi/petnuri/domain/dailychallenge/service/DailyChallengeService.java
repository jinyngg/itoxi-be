package com.itoxi.petnuri.domain.dailychallenge.service;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyAuthImageResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeDetailResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyAuthRepository;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyChallengeRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.global.common.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_DAILY_CHALLENGE_ID;

/**
 * author         : Jisang Lee
 * date           : 2023-09-25
 * description    :
 */
@Service
@RequiredArgsConstructor
public class DailyChallengeService {

    private final DailyChallengeRepository dailyChallengeRepository;
    private final DailyAuthRepository dailyAuthRepository;
    private final MemberRepository memberRepository;

    public List<DailyChallengeListResponse> respDailyChallengeList(Member loginMember) {
        // 반환에 필요한 데이터 : 챌린지 id, 챌리지명, 로그인한 유저의 참여 여부, 썸네일 url
        return dailyChallengeRepository.findAllChallenge(loginMember);
    }

    public Page<DailyAuthImageResponse> respDailyChallengeAuthList(
            Long dailyChallengeId, Member loginMember, Pageable pageable) {
        DailyChallenge dailyChallenge = dailyChallengeRepository.findById(dailyChallengeId)
                .orElseThrow(() -> new Exception400(NOT_FOUND_DAILY_CHALLENGE_ID));
        dailyChallengeRepository.findAllByAuth(dailyChallenge, loginMember, pageable);

        return null;
    }

    public DailyChallengeDetailResponse respDailyChallengeDetail(
            Long dailyChallengeId, Member loginMember) {
        DailyChallenge dailyChallenge = dailyChallengeRepository.findById(dailyChallengeId)
                .orElseThrow(() -> new Exception400(NOT_FOUND_DAILY_CHALLENGE_ID));
//        dailyChallengeRepository.findCha

        return null;
    }

}

