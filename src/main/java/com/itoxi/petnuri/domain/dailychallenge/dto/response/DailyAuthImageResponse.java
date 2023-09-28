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
public class DailyAuthImageResponse {

    private Long challengeId; // 데일리 챌린지 id
    private Long memberId;    // 인증글을 작성한 회원 id
    private String nickName;  // 안증글울 작성한 회원의 닉네임
    private String imageUrl;  // 인증 사진 url
}
