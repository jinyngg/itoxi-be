package com.itoxi.petnuri.domain.eventChallenge.type;

public enum RewardChallengerProcess {
    APPLY,
    KIT_SHIPMENT_PREPARING, // 배송 준비 중
    KIT_SHIPMENT_START,     // 배송 시작
    KIT_SHIPMENT_COMPLETE,  // 배송 완료
    KIT_REVIEW_COMPLETE,
    REWARD_SHIPMENT_PREPARING,
    REWARD_SHIPMENT_START,
    REWARD_SHIPMENT_COMPLETE;
}
