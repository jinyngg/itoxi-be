package com.itoxi.petnuri.global.scheduler;

import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountScheduler {

    private final PetTalkRepository petTalkRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    @Scheduled(cron = "${scheduler.count.petTalk.test.cron}")
    public void deletePostViewCountCacheFromRedis() {
        Set<String> petTalkViewCountKeys = redisTemplate.keys("PetTalkViewCount*");

        if (Objects.requireNonNull(petTalkViewCountKeys).isEmpty()) {
            return;
        }

        for (String petTalkViewCountKey : petTalkViewCountKeys) {
            Long petTalkId = getDomainIdByRedisKey(petTalkViewCountKey);
            Long viewCount = Long.parseLong(String.valueOf(redisTemplate.opsForValue().get(petTalkViewCountKey)));

            // DB 데이터 반영
            petTalkRepository.updateViewCount(petTalkId, viewCount);

            // 캐시 데이터 삭제
            redisTemplate.delete(petTalkViewCountKey);
            redisTemplate.delete("PetTalkViewCount::" + petTalkId);
        }
    }

    private long getDomainIdByRedisKey(String viewCountKey) {
        return Long.parseLong(viewCountKey.split("::")[1]);
    }

}
