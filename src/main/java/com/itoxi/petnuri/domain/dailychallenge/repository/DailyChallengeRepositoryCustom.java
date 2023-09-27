package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.member.entity.Member;

import java.util.List;

/**
 * author         : Jisang Lee
 * date           : 2023-09-25
 * description    :
 */
public interface DailyChallengeRepositoryCustom {

    List<DailyChallengeListResponse> listChallenge(Member loginMember);

}
