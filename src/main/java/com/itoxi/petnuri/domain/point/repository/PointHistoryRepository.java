package com.itoxi.petnuri.domain.point.repository;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * author         : matrix
 * date           : 2023-09-19
 * description    :
 */
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {


    @Query("SELECT CASE WHEN COUNT(ph) > 0 THEN true ELSE false END " +
            "FROM PointHistory ph " +
            "WHERE ph.getMethod = :getMethod " +
            "AND ph.point.member = :member " +
            "AND DATE(ph.createdAt) = DATE(CURRENT_TIMESTAMP)")
    boolean existsTodayAttendanceByGetMethod(
            @Param("getMethod") String getMethod,
            @Param("member") Member member
    );
}
