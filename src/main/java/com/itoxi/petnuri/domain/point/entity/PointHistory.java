package com.itoxi.petnuri.domain.point.entity;

import com.itoxi.petnuri.domain.point.PointStatus;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * author         : matrix
 * date           : 2023-09-18
 * description    :
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "point_history_tb")
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Point point;

    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    @Column(nullable = true)
    private Long getPoint;    // 획득 포인트
    @Column(nullable = true)
    private String getMethod; // 포인트 획득 방법
    @Column(nullable = true)
    private Long usePoint;    // 사용 포인트
    @Column(nullable = true)
    private String useMethod; // 포인트 사용처

    @Column(nullable = false)
    private Long amount;      // 획득/사용 후 잔여 포인트

    public static PointHistory createGetPointHistory(Point point, Long getPoint, String getMethod) {
        point.addPoint(getPoint);

        return PointHistory.builder()
                .point(point)
                .pointStatus(PointStatus.GET)
                .getPoint(getPoint)
                .getMethod(getMethod)
                .amount(point.getHavePoint())
                .build();
    }

    public static PointHistory createUsePointHistory(Point point, Long usePoint, String useMethod) {
        point.usePoint(usePoint);

        return PointHistory.builder()
                .point(point)
                .pointStatus(PointStatus.USE)
                .usePoint(usePoint)
                .useMethod(useMethod)
                .amount(point.getHavePoint())
                .build();
    }
}
