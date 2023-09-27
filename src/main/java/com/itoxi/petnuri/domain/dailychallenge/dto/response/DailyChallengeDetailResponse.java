package com.itoxi.petnuri.domain.dailychallenge.dto.response;

/**
 * author         : Jisang Lee
 * date           : 2023-09-26
 * description    :
 */
public class DailyChallengeDetailResponse {

    private Long id;         // 챌린지 id
    private String banner;   // 챌린지 배너 이미지 url
    private String title;    // 챌린지명
    private String subTitle; // 챌린지 소제목
    private Boolean status;  // 로그인 한 유저의 인증글 작성 여부

}
