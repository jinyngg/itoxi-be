package com.itoxi.petnuri.domain.eventChallenge.dto.request;

import com.itoxi.petnuri.domain.petTalk.type.PetType;
import lombok.Getter;

@Getter
public class WritePointChallengeReviewRequest {

    private PetType petType;
    private String content;

}
