package com.itoxi.petnuri.domain.delivery.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SaveAddressReq {
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

    Boolean isBased;
}
