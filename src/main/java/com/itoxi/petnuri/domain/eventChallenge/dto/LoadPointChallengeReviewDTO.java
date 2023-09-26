package com.itoxi.petnuri.domain.eventChallenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReview;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadPointChallengeReviewDTO {

    private Long id;
    private String photoName;
    private String photoUrl;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static LoadPointChallengeReviewDTO fromEntity(
            PointChallengeReview pointChallengeReview) {
        return LoadPointChallengeReviewDTO.builder()
                .id(pointChallengeReview.getId())
                .photoName(pointChallengeReview.getPhotoName())
                .photoUrl(pointChallengeReview.getPhotoUrl())
                .content(pointChallengeReview.getContent())
                .createdAt(pointChallengeReview.getCreatedAt())
                .build();
    }

}
