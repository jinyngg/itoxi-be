package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyAuthImageResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeDetailResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * author         : Jisang Lee
 * date           : 2023-09-25
 * description    :
 */
public interface DailyChallengeRepositoryCustom {

    List<DailyChallengeListResponse> findAllWithAuthStatus(Member loginMember);

    Page<DailyAuthImageResponse> findAllAuthList(
            DailyChallenge dailyChallenge, Pageable pageable);

    DailyChallengeDetailResponse findDetailChallenge(
            DailyChallenge dailyChallenge, Member loginMember);

}
