package com.itoxi.petnuri.domain.eventChallenge.dto.response;

import com.itoxi.petnuri.domain.eventChallenge.dto.LoadPointChallengeReviewDTO;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReview;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public class LoadPointChallengeReviewsResponse {

    List<LoadPointChallengeReviewDTO> reviews;

    public static LoadPointChallengeReviewsResponse fromEntities(
            List<PointChallengeReview> pointChallengeReviews) {

        return LoadPointChallengeReviewsResponse.builder()
                .reviews(pointChallengeReviews.stream()
                        .map(LoadPointChallengeReviewDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

}
