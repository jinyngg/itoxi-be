package com.itoxi.petnuri.domain.dailychallenge.dto.response;

import com.itoxi.petnuri.domain.dailychallenge.dto.DailyAuthDto;
import com.itoxi.petnuri.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

/**
 * author         : matrix
 * date           : 2023-09-19
 * description    :
 */
@Getter
@Builder
public class DailyAuthResponse {

    private Long challengeId;  // 데일리 챌린지 id
    private Long memberId;     // 인증을 완료한 회원의 id
    private String nickname;   // 인증을 완료한 회원의 닉네임
    private String reviewImg;  // 해당 회원의 인증사진
    private Long memberPoint;  // 해당 회원의 인증 후 보유 포인트

    public static DailyAuthResponse of(DailyAuthDto dailyAutoDto, Member member, Long point) {
        return DailyAuthResponse.builder()
                .challengeId(dailyAutoDto.getChallengeId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .reviewImg(dailyAutoDto.getAuthImageUrl())
                .memberPoint(point)
                .build();
    }
}
