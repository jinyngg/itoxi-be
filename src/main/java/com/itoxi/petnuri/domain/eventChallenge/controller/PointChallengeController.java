package com.itoxi.petnuri.domain.eventChallenge.controller;

import com.itoxi.petnuri.domain.eventChallenge.dto.request.WritePointChallengeReviewRequest;
import com.itoxi.petnuri.domain.eventChallenge.dto.response.LoadPointChallengePostDetailsResponse;
import com.itoxi.petnuri.domain.eventChallenge.dto.response.LoadPointChallengeReviewsResponse;
import com.itoxi.petnuri.domain.eventChallenge.service.PointChallengeReviewService;
import com.itoxi.petnuri.domain.eventChallenge.service.PointChallengeService;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/challenge/point")
@RequiredArgsConstructor
public class PointChallengeController {

    private final PointChallengeService pointChallengeService;
    private final PointChallengeReviewService pointChallengeReviewService;

    @GetMapping("/{pointChallengeId}")
    public ResponseEntity<LoadPointChallengePostDetailsResponse> loadPointChallengePostDetails(
            @PathVariable Long pointChallengeId) {
        LoadPointChallengePostDetailsResponse data =
                LoadPointChallengePostDetailsResponse.fromEntity(
                        pointChallengeService.loadPointChallengePostDetails(pointChallengeId));

        return ResponseEntity.ok(data);
    }

    @GetMapping("/{pointChallengeId}/reviews")
    public ResponseEntity<LoadPointChallengeReviewsResponse> loadPointChallengeReviews(
            @PathVariable Long pointChallengeId,
            Authentication authentication) {
        LoadPointChallengeReviewsResponse data =
                LoadPointChallengeReviewsResponse.fromEntities(
                        pointChallengeReviewService.loadMyReviewsOfThePointChallenge(
                                authentication, pointChallengeId));
        return ResponseEntity.ok(data);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(
            value = "/{pointChallengeId}/review",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> writePointChallengeReview(
            @PathVariable Long pointChallengeId,
            @RequestPart MultipartFile file,
            @RequestPart WritePointChallengeReviewRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        pointChallengeReviewService.write(pointChallengeId, file, request, principalDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
