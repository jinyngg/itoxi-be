package com.itoxi.petnuri.domain.dailychallenge;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
/**
 * author         : matrix
 * date           : 2023-09-13
 * description    :
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "daily_participate")
public class DailyParticipate extends BaseTimeEntity {

    @Id
    @Column(name = "daily_participate_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_challenge_id")
    private DailyChallenge dailyChallenge;  // 챌린지 참여

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String content;

    @Transient
    private Boolean autoSave;  // 펫톡 게시판 자동 등록 여부

    @Transient
    private Boolean status;     // 인증글 작성 여부

    @OneToMany(mappedBy = "dailyParticipate")
    private List<DailyAuth> dailyAuthList = new ArrayList<>();

}
