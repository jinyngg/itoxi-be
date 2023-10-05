package com.itoxi.petnuri.global.scheduler;

import static com.itoxi.petnuri.domain.eventChallenge.type.PointChallengeStatus.CLOSED;
import static com.itoxi.petnuri.domain.eventChallenge.type.PointChallengeStatus.OPENED;
import static com.itoxi.petnuri.domain.eventChallenge.type.PointChallengeStatus.READY;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.INTERNAL_SERVER_ERROR;

import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.PointChallengeReward;
import com.itoxi.petnuri.domain.eventChallenge.repository.PointChallengeRepository;
import com.itoxi.petnuri.domain.eventChallenge.type.PointChallengeStatus;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.component.FileUploadComponent;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventChallengeScheduler {

    private final FileUploadComponent fileUploadComponent;
    private final AmazonS3Service amazonS3Service;
    private final PointChallengeRepository pointChallengeRepository;

    @Transactional
    @Scheduled(cron = "${scheduler.event.point.update.status.test.cron}")
    public void updatePointChallengesStatus() {
        LocalDate currentDate = LocalDate.now();
        List<PointChallenge> pointChallenges = new ArrayList<>();

        for (PointChallenge pointChallenge : pointChallengeRepository.getPointChallengeExceptClosed()) {
            LocalDate startDate = pointChallenge.getStartDate();
            LocalDate endDate = pointChallenge.getEndDate();
            PointChallengeStatus status = pointChallenge.getStatus();

            if (!currentDate.isBefore(startDate) && !currentDate.isAfter(endDate)) {
                if (status == READY) {
                    pointChallenge.updateStatus(OPENED);
                    pointChallenges.add(pointChallenge);
                }

            } else if (currentDate.isAfter(endDate)) {
                if (status == OPENED) {
                    pointChallenge.updateStatus(PointChallengeStatus.CLOSED);
                    pointChallenges.add(pointChallenge);
                }
            }
        }

        pointChallengeRepository.updatePointChallenges(pointChallenges);
    }

    @Transactional
    @Scheduled(cron = "${scheduler.event.point.reward.test.cron}")
    public void payRewardPoints() {
        HashMap<PointChallenge, List<Member>> challengeMembers = new HashMap<>();

        // 1. 진행중인 챌린지 리스트 조회
        List<PointChallenge> pointChallenges =
                pointChallengeRepository.getPointChallengeByStatus(OPENED);

        // 2. 진행중인 챌린지와 포인트 지급 대상인 회원 조회
        for (PointChallenge pointChallenge : pointChallenges) {
            challengeMembers.put(pointChallenge, pointChallengeRepository.getMembersForPointReward(
                    pointChallenge, pointChallenge.getPointMethod().getDay()));
        }

        // 3. 챌린지 리워드 및 포인트 적립
        for (Map.Entry<PointChallenge, List<Member>> entry : challengeMembers.entrySet()) {
            PointChallenge key = entry.getKey();
            List<Member> value = entry.getValue();
            List<PointChallengeReward> pointChallengeRewards = new ArrayList<>();

            for (Member member : value) {
                pointChallengeRewards.add(PointChallengeReward.builder()
                        .pointChallenge(key)
                        .member(member)
                        .build());
            }

            // 3-1. 챌린지 리워드 저장
            pointChallengeRepository.updatePointChallengeRewards(pointChallengeRewards);

            // TODO
            // 3-2. 포인트 적립
        }
    }

    // TODO FLAG 추가(한 번 CSV로 작성된 파일은 더이상 작성하지 않는다.
    @Scheduled(cron = "${scheduler.event.point.upload.csv.test.cron}")
    public void UploadPointChallengeRewardCSV() {
        String pathPrefix = fileUploadComponent.getFileUploadPath();

        // 1. 종료된 챌린지 리스트 조회
        List<PointChallenge> pointChallenges =
                pointChallengeRepository.getPointChallengeByStatus(CLOSED);

        try {
            for (PointChallenge pointChallenge : pointChallenges) {
                List<PointChallengeReward> pointChallengeRewards =
                        pointChallengeRepository.getPointChallengesRewardByPointChallenge(pointChallenge);

                String filename =
                        pathPrefix + pointChallenge.getId();
                File file = new File(filename);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                String lineBreak = System.lineSeparator();

                StringBuilder sb = new StringBuilder();
                sb.append("reward_id").append(",");
                sb.append("challenge_name").append(",");
                sb.append("member_email").append(",");
                sb.append("rewarded_at").append(lineBreak);

                for (PointChallengeReward pointChallengeReward : pointChallengeRewards) {
                    sb.append(pointChallengeReward.getId()).append(",");
                    sb.append(pointChallenge.getTitle()).append(",");
                    sb.append(pointChallengeReward.getMember().getEmail()).append(",");
                    sb.append(pointChallengeReward.getRewardedAt()).append(lineBreak);
                }

                bw.write(sb.toString());
                bw.flush();
                bw.close();

                String url = amazonS3Service.uploadPointChallengeCSV(file);
                log.info("CSV URL : " + url);
                file.delete();

                pointChallenge.save();
                pointChallengeRepository.updatePointChallenge(pointChallenge);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception400(INTERNAL_SERVER_ERROR);
        }
    }
}
