package com.itoxi.petnuri.domain.point.dto.response;

import lombok.*;

/**
 * author         : Jisang Lee
 * date           : 2023-10-05
 * description    :
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PointResponse {

    private Long memberId;
    private String nickname;
    private Long havePoint;

}
