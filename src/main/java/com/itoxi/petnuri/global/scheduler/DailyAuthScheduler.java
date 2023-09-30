package com.itoxi.petnuri.global.scheduler;

import com.itoxi.petnuri.domain.dailychallenge.repository.DailyAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

/**
 * author         : Jisang Lee
 * date           : 2023-09-30
 * description    :
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyAuthScheduler {

    private final DailyAuthRepository dailyAuthRepository;
    private final EntityManager em;

    // Todo: S3 파일 삭제는 S3 서버에서 설정.
    @Transactional
    @Scheduled(cron = "${scheduler.daily-challenge.cron}")
    public Long deleteDailyAuthDataByDate() {
        Long deletedCount = dailyAuthRepository.deleteAuthDataByDate();
        log.info(deletedCount + " 건 삭제 완료. " + LocalDateTime.now());
        
        em.flush();
        em.clear();
        return deletedCount;
    }
}
