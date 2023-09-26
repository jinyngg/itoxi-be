package com.itoxi.petnuri.domain.eventChallenge.dto.response;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.type.PointChallengeStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadPointChallengePostDetailsResponse {

    private Long id;

    private String thumbnail;
    private String poster;

    private String title;
    private String subTitle;

    private String guide;
    private String notice;

    private Long point;

    private PointChallengeStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    public static LoadPointChallengePostDetailsResponse fromEntity(PointChallenge pointChallenge) {
        return LoadPointChallengePostDetailsResponse.builder()
                .id(pointChallenge.getId())
                .thumbnail(pointChallenge.getThumbnail())
                .poster(pointChallenge.getPoster())
                .title(pointChallenge.getTitle())
                .subTitle(pointChallenge.getSubTitle())
                .guide(pointChallenge.getPointMethod().getDescription())
                .notice(pointChallenge.getNotice())
                .point(pointChallenge.getPoint())
                .status(pointChallenge.getStatus())
                .startDate(pointChallenge.getStartDate())
                .endDate(pointChallenge.getEndDate())
                .build();
    }

}
