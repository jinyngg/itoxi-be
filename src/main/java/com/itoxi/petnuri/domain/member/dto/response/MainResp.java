package com.itoxi.petnuri.domain.member.dto.response;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.member.entity.Pet;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MainResp {
    private final MainContentDTO content;

    public MainResp(MainContentDTO content) {
        this.content = content;
    }

    @Getter
    public static class MainContentDTO {
        private final List<PetDTO> petList;
        private final ChallengeDTO challengeList;
        private final List<PetTalkDTO> petTalkList;

        public MainContentDTO(List<PetDTO> petList, ChallengeDTO challengeList, List<PetTalkDTO> petTalkList) {
            this.petList = petList;
            this.challengeList = challengeList;
            this.petTalkList = petTalkList;
        }
    }

    @Getter
    public static class PetDTO {
        private final Long id;
        private final String petName;
        private final String image;
        private final String petGender;
        private final Integer petAge;
        private final Boolean isSelected;

        public PetDTO(Pet pet) {
            this.id = pet.getId();
            this.petName = pet.getPetName();
            this.image = pet.getImage();
            this.petGender = pet.getPetGender().getLabel();
            this.petAge = pet.getPetAge();
            this.isSelected = pet.getIsSelected();
        }
    }

    @Getter
    public static class ChallengeDTO {
        private final List<RewardChallengeDTO> rewardChallengeList;
        private final DailyChallengeDTO dailyChallenge;

        public ChallengeDTO(List<RewardChallengeDTO> rewardChallengeList, DailyChallengeDTO dailyChallenge) {
            this.rewardChallengeList = rewardChallengeList;
            this.dailyChallenge = dailyChallenge;
        }
    }

    @Getter
    public static class RewardChallengeDTO {
        private final Long id;
        private final String title;
        private final String subTitle;
        private final String thumbnail;

        public RewardChallengeDTO(RewardChallenge rewardChallenge) {
            this.id = rewardChallenge.getId();
            this.title = rewardChallenge.getTitle();
            this.subTitle = rewardChallenge.getSubTitle();
            this.thumbnail = rewardChallenge.getThumbnail();
        }
    }

    @Getter
    public static class DailyChallengeDTO {
        private final Long id;
        private final String title;
        private final String subTitle;
        private final String thumbnail;

        public DailyChallengeDTO(DailyChallenge dailyChallenge) {
            this.id = dailyChallenge.getId();
            this.title = dailyChallenge.getTitle();
            this.subTitle = dailyChallenge.getSubTitle();
            this.thumbnail = dailyChallenge.getThumbnail();
        }
    }

    @Getter
    public static class PetTalkDTO {
        private final Long id;
        private final String title;
        private final String writer;
        private final String thumbnail;
        private final LocalDateTime createdAt;

        public PetTalkDTO(PetTalk petTalk) {
            this.id = petTalk.getId();
            this.title = petTalk.getTitle();
            this.writer = petTalk.getWriter().getNickname();
            this.thumbnail = petTalk.getThumbnail();
            this.createdAt = petTalk.getCreatedAt();
        }
    }
}
