package com.itoxi.petnuri.domain.point.repository;

import com.itoxi.petnuri.domain.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author         : matrix
 * date           : 2023-09-19
 * description    :
 */
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
