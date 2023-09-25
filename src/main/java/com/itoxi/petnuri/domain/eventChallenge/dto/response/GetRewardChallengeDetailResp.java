package com.itoxi.petnuri.domain.eventChallenge.dto.response;

import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.eventChallenge.type.RewardChallengeStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetRewardChallengeDetailResp {
    private final Long id;
    private final String title;
    private final String subTitle;
    private final String notice;
    private final String thumbnail;
    private final String poster;
    private final RewardChallengeStatus status;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final LocalDateTime kitStartDate;
    private final LocalDateTime kitEndDate;
    private final LocalDateTime reviewStartDate;
    private final LocalDateTime reviewEndDate;

    public GetRewardChallengeDetailResp(RewardChallenge rewardChallenge) {
        this.id = rewardChallenge.getId();
        this.title = rewardChallenge.getTitle();
        this.subTitle = rewardChallenge.getSubTitle();
        this.notice = rewardChallenge.getNotice();
        this.thumbnail = rewardChallenge.getThumbnail();
        this.poster = rewardChallenge.getPoster();
        this.status = rewardChallenge.getStatus();
        this.startDate = rewardChallenge.getStartDate();
        this.endDate = rewardChallenge.getEndDate();
        this.kitStartDate = rewardChallenge.getKitStartDate();
        this.kitEndDate = rewardChallenge.getKitEndDate();
        this.reviewStartDate = rewardChallenge.getReviewStartDate();
        this.reviewEndDate = rewardChallenge.getReviewEndDate();
    }
}
