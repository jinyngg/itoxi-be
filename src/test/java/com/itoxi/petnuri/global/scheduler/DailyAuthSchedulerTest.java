package com.itoxi.petnuri.global.scheduler;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyAuthRepository;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyChallengeRepository;
import com.itoxi.petnuri.domain.dailychallenge.type.ChallengeStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.NoSuchElementException;
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
    @Autowired
    private DailyChallengeRepository dailyChallengeRepository;
    @Autowired
    private DailyAuthRepository dailyAuthRepository;
    @Autowired
    private EntityManager em;

    @DisplayName("하루 이상 지난 인증글 삭제 테스트")
    @Test
    @Sql(scripts = {"classpath:create_auth.sql"})
    public void dailyAuthDelete_test() {
        // Given
        long count = dailyAuthRepository.count();
        System.out.println("테스트 : Current auth data count = " + count);

        // When & Then
        await()
                .atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    dailyAuthScheduler.deleteDailyAuthDataByTodayBefore();
                    long result = dailyAuthRepository.count();
                    assertThat(result).isEqualTo(0);
                });
    }

    @DisplayName("READY 상태인 데일리 챌린지의 OPENED 업데이트 테스트")
    @Test
    public void dailyChallengeUpdate_test() throws Exception {
        // given
        DailyChallenge dailyChallenge1 = DailyChallenge.builder()
                .title("놀아주기 챌린지")
                .subTitle("반려동물과 즐거운 시간을 보내는 사진을 인증해요!")
                .authMethod("인증 사진 업로드!")
                .payment(100L)
                .paymentMethod("참여완료 즉시 지급")
                .thumbnail("https://www.test.url/thumbnail.jpg")
                .banner("https://www.test.url/banner.jpg")
                .challengeStatus(ChallengeStatus.READY)
                .startDate(LocalDate.now())
                .endDate(LocalDate.of(9999, 12, 31))
                .build();
        DailyChallenge dailyChallenge2 = DailyChallenge.builder()
                .title("위생관리 챌린지")
                .subTitle("반려동물의 위생/청결관리 사진을 인증해요!")
                .authMethod("인증 사진 업로드!")
                .payment(100L)
                .paymentMethod("참여완료 즉시 지급")
                .thumbnail("https://www.test.url/thumbnail.jpg")
                .banner("https://www.test.url/banner.jpg")
                .challengeStatus(ChallengeStatus.READY)
                .startDate(LocalDate.now())
                .endDate(LocalDate.of(9999, 12, 31))
                .build();
        dailyChallengeRepository.save(dailyChallenge1);
        dailyChallengeRepository.save(dailyChallenge2);
        em.flush();
        em.clear();

        // when
        long result = dailyAuthScheduler.openDailyChallengeByStartDateAfter();
        DailyChallenge challenge = dailyChallengeRepository.findById(dailyChallenge1.getId())
                .orElseThrow(NoSuchElementException::new);

        // then
        System.out.println("테스트 : result = " + result);
        System.out.println("테스트 : status = " + challenge.getChallengeStatus());
        assertThat(result).isGreaterThan(0);
        assertThat(challenge.getChallengeStatus()).isEqualTo(ChallengeStatus.OPENED);

    }

}
