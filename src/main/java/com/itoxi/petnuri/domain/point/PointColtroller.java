package com.itoxi.petnuri.domain.point;

import com.itoxi.petnuri.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author         : matrix
 * date           : 2023-09-19
 * description    :
 */
@RequestMapping("/point")
@RestController
@RequiredArgsConstructor
public class PointColtroller {

    private final PointService pointService;

}
