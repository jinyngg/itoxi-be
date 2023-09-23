package com.itoxi.petnuri.domain.point.entity;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import com.itoxi.petnuri.global.common.exception.Exception400;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.OUT_OF_POINT;

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
    private Long havePoint;    // 현재 보유 포인트

    public Point addPoint(Long point) {
        this.havePoint += point;
        return this;
    }

    public Point usePoint(Long point) {
        isEnoughTotalPoint(point); // 보유 포인트가 충분한지 확인
        this.havePoint -= point;
        return this;
    }

    private void isEnoughTotalPoint(Long point) {
        if (this.havePoint < point) {
            throw new Exception400(OUT_OF_POINT);
        }
    }

    public static Point createPoint(Member member) {
        return Point.builder()
                .member(member)
                .havePoint(0L)
                .build();
    }
}
