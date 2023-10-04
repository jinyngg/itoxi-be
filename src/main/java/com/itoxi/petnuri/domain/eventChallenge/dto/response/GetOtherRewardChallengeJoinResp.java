package com.itoxi.petnuri.domain.eventChallenge.dto.response;

import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallengeReview;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenger;
import lombok.Getter;

import java.util.List;

@Getter
public class GetOtherRewardChallengeJoinResp {
    private final List<JoinMemberDTO> data;

    public GetOtherRewardChallengeJoinResp(List<JoinMemberDTO> data) {
        this.data = data;
    }

    @Getter
    public static class JoinMemberDTO {
        private final Long memberId;
        private final String nickName;
        private final String imageUrl;

        public JoinMemberDTO(RewardChallenger challenger) {
            this.memberId = challenger.getChallenger().getId();
            this.nickName = challenger.getChallenger().getNickname();
            this.imageUrl = challenger.getChallenger().getProfileImageUrl();
        }

        public JoinMemberDTO(RewardChallengeReview review) {
            this.memberId = review.getChallenger().getId();
            this.nickName = review.getChallenger().getNickname();
            this.imageUrl = review.getPhotoUrl();
        }
    }
}
