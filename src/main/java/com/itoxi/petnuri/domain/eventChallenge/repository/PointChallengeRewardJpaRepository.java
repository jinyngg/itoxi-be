package com.itoxi.petnuri.domain.eventChallenge.repository;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReward;
import com.itoxi.petnuri.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointChallengeRewardJpaRepository extends JpaRepository<PointChallengeReward, Long> {

    List<PointChallengeReward> findAllByPointChallenge(PointChallenge pointChallenge);

}