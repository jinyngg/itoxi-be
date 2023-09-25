package com.itoxi.petnuri.domain.delivery.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ChallengeDeliveryDTO {
    @NotNull
    String name;

    @NotNull
    String phone;

    @NotNull
    String roadAddress;

    @NotNull
    String address;

    @NotNull
    String zipcode;

    @NotNull
    String message;
}
