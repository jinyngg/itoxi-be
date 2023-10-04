package com.itoxi.petnuri.global.scheduler;

import com.itoxi.petnuri.domain.dailychallenge.repository.DailyAuthRepository;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * author         : Jisang Lee
 * date           : 2023-09-30
 * description    :
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DailyAuthScheduler {

    private final DailyAuthRepository dailyAuthRepository;
    private final DailyChallengeRepository dailyChallengeRepository;
    private final EntityManager em;

    // Todo: S3 파일 삭제는 S3 서버에서 설정.
    @Scheduled(cron = "${scheduler.daily-challenge.cron}")
    public void deleteDailyAuthDataByTodayBefore() {
        dailyAuthRepository.deleteDailyAuthByUpdatedAtBefore(
                LocalDate.now().atStartOfDay());
        log.info(LocalDate.now() + "일자 이전의 인증글이 삭제 되었습니다. " + LocalDateTime.now());
    }

    @Scheduled(cron = "${scheduler.daily-challenge.cron}")
    @Modifying(clearAutomatically = true)
    public long openDailyChallengeByStartDateAfter() {
        em.flush();
        long count = dailyChallengeRepository.updateDailyChallenge();
        if (count < 1) {
            log.info("오픈된 데일리 챌린지가 없습니다. " + LocalDateTime.now());
            return count;
        }
        log.info(count + " 건의 데일리 챌린지가 OPEN 되었습니다. " + LocalDateTime.now());
        return count;
    }

}
