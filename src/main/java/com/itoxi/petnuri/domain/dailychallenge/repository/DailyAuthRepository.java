package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * author         : matrix
 * date           : 2023-09-16
 * description    :
 */
public interface DailyAuthRepository extends JpaRepository<DailyAuth, Long>, DailyAuthRepositoryCustom {

    long deleteDailyAuthByUpdatedAtBefore(LocalDateTime today);

}
