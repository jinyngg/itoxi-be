package com.itoxi.petnuri.domain.delivery.service;

import com.itoxi.petnuri.domain.delivery.dto.response.DeliveryListRes;
import com.itoxi.petnuri.domain.delivery.repository.DeliveryAddressRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.delivery.dto.request.SaveAddressReq;
import com.itoxi.petnuri.domain.delivery.dto.request.UpdateAddressReq;
import com.itoxi.petnuri.domain.delivery.entity.DeliveryAddress;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryAddressRepository deliveryAddressRepository;

    public List<DeliveryListRes> getDeliveryAddressList(Member member){
        List<DeliveryAddress> deliveryAddressList = deliveryAddressRepository.findAllByMember(member);
        List<DeliveryListRes> deliveryListResList = new ArrayList<>();

        for(DeliveryAddress deliveryAddress : deliveryAddressList){
            deliveryListResList.add(DeliveryListRes.builder()
                    .id(deliveryAddress.getId())
                    .name(deliveryAddress.getName())
                    .phone(deliveryAddress.getPhone())
                    .roadAddress(deliveryAddress.getRoadAddress())
                    .address(deliveryAddress.getAddress())
                    .zipcode(deliveryAddress.getZipcode())
                    .isBased(deliveryAddress.getIsBased()).build());
        }
        return deliveryListResList;
    }

    @Transactional
    public void deleteDeliveryAddress(Long deliveryAddressId) {
        deliveryAddressRepository.deleteById(deliveryAddressId);

        List<DeliveryAddress> deliveryAddressList = deliveryAddressRepository.findAll();

        for(DeliveryAddress deliveryAddress : deliveryAddressList){
            deliveryAddress.updateIsBased(true);
        }
    }

    @Transactional
    public void save(Member member, SaveAddressReq request) {
        if (deliveryAddressRepository.countByMember(member) >= 2) {
            throw new Exception400(ErrorCode.FULL_ADDRESS);
        }
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findByMember(member)
                .orElse(null);

        if (deliveryAddress == null) {
            deliveryAddressRepository.save(
                    DeliveryAddress.create(
                            member, request.getName(), request.getPhone(),
                            request.getRoadAddress(), request.getAddress(), request.getZipcode(), Boolean.TRUE
                    )
            );

            return;
        }

        if (request.getIsBased()) {
            deliveryAddress.updateIsBased(false);
            deliveryAddressRepository.save(deliveryAddress);
        }

        deliveryAddressRepository.save(
                DeliveryAddress.create(
                        member, request.getName(), request.getPhone(),
                        request.getRoadAddress(), request.getAddress(), request.getZipcode(), request.getIsBased()
                )
        );
    }

    @Transactional
    public void update(Member member, UpdateAddressReq request) {
        List<DeliveryAddress> deliveryAddressList = deliveryAddressRepository.findAllByMember(member);
        for (DeliveryAddress deliveryAddress : deliveryAddressList) {
            if (request.getIsBased()) {
                deliveryAddress.updateIsBased(false);
            }

            if (deliveryAddress.getId().equals(request.getId())) {
                deliveryAddress.updateAddress(
                        request.getName(), request.getPhone(), request.getRoadAddress(),
                        request.getAddress(), request.getZipcode(), request.getIsBased()
                );
            }
        }

        deliveryAddressRepository.saveAll(deliveryAddressList);
    }
}
