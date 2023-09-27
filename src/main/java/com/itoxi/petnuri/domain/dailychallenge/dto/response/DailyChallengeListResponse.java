package com.itoxi.petnuri.domain.dailychallenge.dto.response;

import lombok.*;

/**
 * author         : Jisang Lee
 * date           : 2023-09-25
 * description    :
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DailyChallengeListResponse {

    private Long challengeId; // 챌린지 id
    private String title;     // 챌린지명
    private String thumbnail; // 챌린지 썸네일
    private Boolean status;   // 유저의 인증글 작성 여부

}
