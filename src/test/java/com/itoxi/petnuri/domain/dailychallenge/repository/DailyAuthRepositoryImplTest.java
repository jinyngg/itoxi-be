package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyAuth;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * author         : Jisang Lee
 * date           : 2023-09-26
 * description    :
 */
@SpringBootTest
@Transactional
class DailyAuthRepositoryImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DailyAuthRepository dailyAuthRepository;

    @Autowired
    private DailyChallengeRepository dailyChallengeRepository;

    @DisplayName("데일리 챌린지 중복 인증 방어 테스트")
    @Test
    public void dupePostByOneMember_and_querydslDateCompare_test() throws Exception {
        // given
        Member member1 = Member.createMember("tester1@test.copm", "tester1", "test1");
        Member member2 = Member.createMember("tester2@test.copm", "tester2", "test2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        DailyChallenge challenge1 = dailyChallengeRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        DailyAuth auth1 = DailyAuth.createDailyAuth(member1, challenge1, "https://test.url/test.jpng");
        dailyAuthRepository.save(auth1);

        // when
        boolean result1 = dailyAuthRepository.dupePostCheck(member1, challenge1);
        boolean result2 = dailyAuthRepository.dupePostCheck(member2, challenge1);
        System.out.println("테스트 : result = " + result1);
        System.out.println("테스트 : result = " + result2);

        // then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    public void authImagePage_test() throws Exception {
        // given
        Long challengeId = 1L;
        DailyChallenge dailyChallenge = dailyChallengeRepository.findById(challengeId)
                .orElseThrow(NoSuchElementException::new);
//        Page<List<DailyAuth>> result = dailyAuthRepository.findAllByDailyChallengeId(dailyChallenge.getId());

        // when

        // then
    }
}