package com.itoxi.petnuri.domain.dailychallenge.repository;

import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyAuthImageResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeDetailResponse;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyChallengeListResponse;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyAuth;
import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.type.ChallengeStatus;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * author         : Jisang Lee
 * date           : 2023-09-26
 * description    :
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class DailyChallengeRepositoryImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DailyAuthRepository dailyAuthRepository;

    @Autowired
    private DailyChallengeRepository dailyChallengeRepository;

    @DisplayName("챌린지 메인 화면 - 챌린지 목록 응답 테스트")
    @Test
    public void findAllChallenge_test() throws Exception {
        // given
        Member member1 = Member.createMember("tester1@test.copm", "tester1", "test1");
        Member member2 = Member.createMember("tester2@test.copm", "tester2", "test2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        DailyChallenge challenge1 = dailyChallengeRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        DailyChallenge challenge2 = DailyChallenge.builder()
                .title("놀아주기 챌린지")
                .subTitle("반려동물에게 간식주는 사진을 인증해요!")
                .authMethod("인증 사진 업로드!")
                .payment(100L)
                .paymentMethod("참여완료 즉시 지급")
                .thumbnail("https://www.test.url/thumbnail.jpng")
                .banner("https://www.test.url/banner.jpng")
                .challengeStatus(ChallengeStatus.OPENED)
                .startDate(LocalDate.now())
                .endDate(LocalDate.of(9999, 12, 31))
                .build();
        DailyChallenge challenge3 = DailyChallenge.builder()
                .title("위생관리 챌린지")
                .subTitle("반려동물의 위생/청결관리 사진을 인증해요!")
                .authMethod("인증 사진 업로드!")
                .payment(100L)
                .paymentMethod("참여완료 즉시 지급")
                .thumbnail("https://www.test.url/thumbnail.jpng")
                .banner("https://www.test.url/banner.jpng")
                .challengeStatus(ChallengeStatus.OPENED)
                .startDate(LocalDate.now())
                .endDate(LocalDate.of(9999, 12, 31))
                .build();
        dailyChallengeRepository.save(challenge2);
        dailyChallengeRepository.save(challenge3);

        DailyAuth auth1 = DailyAuth.toEntity(member1, challenge1, "https://test.url/test.jpng");
        DailyAuth auth2 = DailyAuth.toEntity(member1, challenge2, "https://test.url/test.jpng");
        DailyAuth auth3 = DailyAuth.toEntity(member1, challenge3, "https://test.url/test.jpng");
        dailyAuthRepository.save(auth1);
        dailyAuthRepository.save(auth2);
        dailyAuthRepository.save(auth3);

        // when
        List<DailyChallengeListResponse> result1 = dailyChallengeRepository.findAllWithAuthStatus(member1);
        List<DailyChallengeListResponse> result2 = dailyChallengeRepository.findAllWithAuthStatus(member2);

        for (DailyChallengeListResponse response : result1) {
            System.out.println("테스트 : challengeId = " + response.getChallengeId());
            System.out.println("테스트 : title = " + response.getTitle());
            System.out.println("테스트 : status = " + response.getStatus());
            System.out.println("테스트 : thumbnail = " + response.getThumbnail());
        }

        System.out.println("==============================================================");

        for (DailyChallengeListResponse response : result2) {
            System.out.println("테스트 : challengeId = " + response.getChallengeId());
            System.out.println("테스트 : title = " + response.getTitle());
            System.out.println("테스트 : status = " + response.getStatus());
            System.out.println("테스트 : thumbnail = " + response.getThumbnail());
        }

        // then
        assertThat(result1.get(0).getStatus()).isTrue();
        assertThat(result2.get(0).getStatus()).isFalse();
    }

    @DisplayName("데일리 챌린지 Detail view 응답 데이터 테스트")
    @Test
    public void findDetailChallenge_test() throws Exception {
        // given
        Member member1 = Member.createMember("tester1@test.copm", "tester1", "test1");
        Member member2 = Member.createMember("tester2@test.copm", "tester2", "test2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Long challengeId1 = 1L;
        DailyChallenge challenge1 = dailyChallengeRepository.findById(challengeId1).orElseThrow(NoSuchElementException::new);

        DailyAuth auth1 = DailyAuth.toEntity(member1, challenge1, "https://test.url/test.jpng");
        dailyAuthRepository.save(auth1);

        // when
        DailyChallengeDetailResponse response1 = dailyChallengeRepository.findDetailChallenge(challengeId1, member1);
        DailyChallengeDetailResponse response2 = dailyChallengeRepository.findDetailChallenge(challengeId1, member2);

        // then
        assertThat(response1.getStatus()).isTrue();
        assertThat(response2.getStatus()).isFalse();
    }

    @DisplayName("데일리 챌린지 디테일 view - 인증 사진 목록 응답 테스트")
    @Test
    public void findAllAuthList_test() throws Exception {
        // given
        Member member1 = Member.createMember("tester1@test.copm", "tester1", "test1");
        Member member2 = Member.createMember("tester2@test.copm", "tester2", "test2");
        Member member3 = Member.createMember("tester3@test.copm", "tester3", "test3");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Long challengeId1 = 1L;
        DailyChallenge challenge1 = dailyChallengeRepository.findById(challengeId1).orElseThrow(NoSuchElementException::new);

        DailyAuth auth1 = DailyAuth.toEntity(member1, challenge1, "https://test.url/test.jpng");
        DailyAuth auth2 = DailyAuth.toEntity(member2, challenge1, "https://test.url/test.jpng");
        DailyAuth auth3 = DailyAuth.toEntity(member3, challenge1, "https://test.url/test.jpng");
        dailyAuthRepository.save(auth1);
        dailyAuthRepository.save(auth2);
        dailyAuthRepository.save(auth3);

        // when
        Pageable pageable = PageRequest.of(0, 5);
        Page<DailyAuthImageResponse> responses = dailyChallengeRepository.findAllAuthList(challengeId1, pageable);

        for (DailyAuthImageResponse respons : responses) {
            System.out.println("테스트 : challengeId =  " + respons.getChallengeId());
            System.out.println("테스트 : memberId = " + respons.getMemberId());
            System.out.println("테스트 : nickName = " + respons.getNickName());
            System.out.println("테스트 : imageUrl = " + respons.getImageUrl());
            System.out.println("----------------------------------------------------------------");
        }

        // then
        assertThat(responses.getTotalElements()).isEqualTo(3);
    }

}