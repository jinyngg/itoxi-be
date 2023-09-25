package com.itoxi.petnuri.domain.eventChallenge.dto.request;

import com.itoxi.petnuri.domain.delivery.dto.ChallengeDeliveryDTO;
import com.itoxi.petnuri.global.common.customValid.valid.ValidId;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CreateChallengerReq {
    @ValidId
    Long rewardId;

    @NotNull
    Boolean isConsentedPersonalInfo;

    @NotNull
    ChallengeDeliveryDTO delivery;
}
