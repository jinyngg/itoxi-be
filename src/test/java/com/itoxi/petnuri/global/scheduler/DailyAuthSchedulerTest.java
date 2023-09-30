package com.itoxi.petnuri.global.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * author         : Jisang Lee
 * date           : 2023-10-01
 * description    :
 */
@SpringBootTest(properties = {"scheduler.daily-challenge.cron=1 * * * * *"})
@Transactional
class DailyAuthSchedulerTest {

    @Autowired
    private DailyAuthScheduler dailyAuthScheduler;

    @Test
    public void scheduler_test() {
        await()
                .atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Long result = dailyAuthScheduler.deleteDailyAuthDataByDate();
                    assertThat(result).isGreaterThan(0);
                });
    }

}