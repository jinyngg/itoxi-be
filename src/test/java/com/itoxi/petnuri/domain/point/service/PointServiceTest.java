package com.itoxi.petnuri.domain.point.service;

import com.itoxi.petnuri.domain.point.repository.PointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * author         : matrix
 * date           : 2023-09-19
 * description    :
 */
@SpringBootTest
@Transactional
class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Test
    public void pointAdd_test() throws Exception {
        // given
        Long challengeId = 1L;
        Long userId = 1L;

        // when
        Long testPoint = pointService.getPoint(challengeId, userId);

        // then
        System.out.println("테스트 : point = " + testPoint);
        assertThat(testPoint).isEqualTo(100);
    }

}