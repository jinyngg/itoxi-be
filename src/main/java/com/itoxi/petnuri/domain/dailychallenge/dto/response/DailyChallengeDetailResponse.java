package com.itoxi.petnuri.domain.dailychallenge.dto.response;

import lombok.*;

/**
 * author         : Jisang Lee
 * date           : 2023-09-26
 * description    :
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DailyChallengeDetailResponse {

    private Long challengeId;    // 챌린지 id
    private String banner;       // 챌린지 배너 이미지 url
    private String title;        // 챌린지명
    private String subTitle;     // 챌린지 소제목
    private String authMethod;   // 챌린지 인증방법
    private Long point;         // 챌린지 리워드 포인트
    private String pointMethod; // 포인트 지급 방법
    private Boolean status;      // 로그인 한 유저의 인증글 작성 여부

}
