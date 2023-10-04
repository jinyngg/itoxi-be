package com.itoxi.petnuri.domain.point.repository;

import com.itoxi.petnuri.domain.point.dto.response.PointResponse;

/**
 * author         : Jisang Lee
 * date           : 2023-10-05
 * description    :
 */
public interface PointRepositoryCustom {

    PointResponse findPointByMemberId(Long memberId);

}
