package com.itoxi.petnuri.domain.point.controller;

import com.itoxi.petnuri.domain.point.dto.response.PointResponse;
import com.itoxi.petnuri.domain.point.service.PointService;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
public class PointController {

    private final PointService pointService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/get")
    public ResponseEntity getPoint(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PointResponse response = pointService.getPointResponse(principalDetails.getMember().getId());
        return ResponseEntity.ok(response);
    }

}
