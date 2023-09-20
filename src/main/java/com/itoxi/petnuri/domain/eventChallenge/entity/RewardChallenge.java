package com.itoxi.petnuri.domain.eventChallenge.entity;

import static com.itoxi.petnuri.domain.eventChallenge.type.RewardChallengeStatus.ACTIVE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itoxi.petnuri.domain.eventChallenge.type.RewardChallengeStatus;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reward_challenge")
public class RewardChallenge extends BaseTimeEntity {

    @Id
    @Column(name = "reward_challenge_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "notice")
    private String notice;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "poster", nullable = false)
    private String poster;

    @Builder.Default
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RewardChallengeStatus status = ACTIVE;

    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    @Column(name = "kit_start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime kitStartDate;

    @Column(name = "kit_end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime kitEndDate;

    @Column(name = "review_start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime reviewStartDate;

    @Column(name = "review_end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime reviewEndDate;

    @OneToMany(mappedBy = "rewardChallenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RewardChallenger> rewardChallengers = new ArrayList<>();

    @OneToMany(mappedBy = "rewardChallenge")
    private List<RewardChallengeReview> rewardChallengeReviews = new ArrayList<>();

}
