package com.itoxi.petnuri.domain.point.repository;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.domain.point.dto.response.PointResponse;
import com.itoxi.petnuri.domain.point.entity.Point;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * author         : Jisang Lee
 * date           : 2023-10-05
 * description    :
 */
//@AutoConfigureMockMvc
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class PointRepositoryImplTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void pointViewResponse_test() throws Exception {
        // given
        Member member = Member.createMember("test@test.com", "tester", "test");
        memberRepository.save(member);
        Point point = Point.createPoint(member);
        pointRepository.save(point);
        em.flush();
        em.clear();

        // when
        PointResponse result = pointRepository.findPointByMemberId(member.getId());

        System.out.println("테스트 : memberId = " + result.getMemberId());
        System.out.println("테스트 : nickname = " + result.getNickname());
        System.out.println("테스트 : point = " + result.getHavePoint());

        // Then
        assertThat(result.getMemberId()).isEqualTo(member.getId());
        assertThat(result.getNickname()).isEqualTo(member.getNickname());
        assertThat(result.getHavePoint()).isEqualTo(point.getHavePoint());
    }

}