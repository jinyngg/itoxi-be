package com.itoxi.petnuri.domain.point.repository;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * author         : matrix
 * date           : 2023-09-19
 * description    :
 */
public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByMember(Member member);

}
