package com.itoxi.petnuri.domain.point.entity;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * author         : matrix
 * date           : 2023-09-16
 * description    :
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "point_tb")
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "total_point", nullable = false)
    private Long totalPoint;    // 현재 보유 포인트

    public void addPoint(Long point) {
        this.totalPoint += point;
    }

    public void usePoint(Long point) {
        this.totalPoint -= point;
    }

    public static Point createPoint(Member member) {
        return Point.builder()
                .member(member)
                .totalPoint(0L)
                .build();
    }
}
