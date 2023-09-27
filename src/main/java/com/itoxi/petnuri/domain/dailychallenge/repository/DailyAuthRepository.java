package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyAuth;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author         : matrix
 * date           : 2023-09-16
 * description    :
 */
public interface DailyAuthRepository extends JpaRepository<DailyAuth, Long>, DailyAuthRepositoryCustom {
    
}
