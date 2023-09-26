package com.itoxi.petnuri.domain.delivery.controller;

import com.itoxi.petnuri.domain.delivery.dto.request.SaveAddressReq;
import com.itoxi.petnuri.domain.delivery.dto.request.UpdateAddressReq;
import com.itoxi.petnuri.domain.delivery.service.DeliveryService;
import com.itoxi.petnuri.domain.member.entity.Member;
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
    @DeleteMapping("/address/{deliveryAddressId}")
    public ResponseEntity deleteDeliveryAddress(@PathVariable Long deliveryAddressId) {
        deliveryService.deleteDeliveryAddress(deliveryAddressId);
        return new ResponseEntity("삭제 완료", HttpStatus.OK);
    }

    @PostMapping("/address")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> saveAddress(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody SaveAddressReq request
    ) {
        Member member = principalDetails.getMember();
        deliveryService.save(member, request);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/address")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> updateAddress(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody UpdateAddressReq request
    ) {
        Member member = principalDetails.getMember();
        deliveryService.update(member, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
