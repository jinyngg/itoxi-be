package com.itoxi.petnuri.domain.point.entity;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private Member member;

    @Column(nullable = false)
    private Long totalPoint;

    @OneToMany(mappedBy = "point")
    private List<GetPoint> getPoints = new ArrayList<>();

    @OneToMany(mappedBy = "")
    private List<UsePoint> usePoints = new ArrayList<>();

}
