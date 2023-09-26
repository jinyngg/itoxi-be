package com.itoxi.petnuri.domain.eventChallenge.entity;

import static com.itoxi.petnuri.domain.eventChallenge.type.PointChallengeStatus.READY;

import com.itoxi.petnuri.domain.eventChallenge.type.PointChallengeStatus;
import com.itoxi.petnuri.domain.eventChallenge.type.PointMethod;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "point_challenge")
public class PointChallenge extends BaseTimeEntity {

    @Id
    @Column(name = "point_challenge_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "poster", nullable = false)
    private String poster;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "notice")
    private String notice;

    @Column(name = "point", nullable = false)
    private Long point;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_method", nullable = false)
    private PointMethod pointMethod;

    @Builder.Default
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PointChallengeStatus status = READY;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public void uploadThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void uploadPoster(String poster) {
        this.poster = poster;
    }


}
