package com.itoxi.petnuri.domain.delivery.controller;

import com.itoxi.petnuri.domain.delivery.service.DeliveryService;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/address")
    public ResponseEntity getDeliveryAddressList(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return new ResponseEntity(deliveryService.getDeliveryAddressList(principalDetails.getMember()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/address")
    public ResponseEntity deleteDeliveryAddress(@RequestParam Long deliveryAddressId){
        deliveryService.deleteDeliveryAddress(deliveryAddressId);
        return new ResponseEntity("삭제 완료", HttpStatus.OK);
    }
}
