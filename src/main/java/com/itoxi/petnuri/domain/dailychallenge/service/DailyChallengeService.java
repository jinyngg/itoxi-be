package com.itoxi.petnuri.domain.dailychallenge.service;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyAuthImageResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyAuthRepository;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyChallengeRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(NoSuchElementException::new);
        System.out.printf("테스트 : loginMember = %d, %s%n", member.getId(), member.getNickname());

        List<DailyChallengeListResponse> allWithMember = dailyChallengeRepository.listChallenge(loginMember);
        for (DailyChallengeListResponse response : allWithMember) {
            System.out.println(response);
        }
        return allWithMember;
    }

    public Page<List<DailyAuthImageResponse>> respDailyChallengeAuthList(Long dailyChallengeId) {

        return null;
    }
}

