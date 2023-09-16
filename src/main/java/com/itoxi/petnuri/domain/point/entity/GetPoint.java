package com.itoxi.petnuri.domain.point.entity;

import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.*;

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
@Table(name = "get_point")
public class GetPoint extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "get_point_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    @Column(nullable = false)
    private Long getPoint; // 획득한 포인트

    @Column(nullable = false)
    private String getMethod; // 포인트 획득 방법
}
