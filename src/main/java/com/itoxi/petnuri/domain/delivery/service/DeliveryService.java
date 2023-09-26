package com.itoxi.petnuri.domain.delivery.service;

import com.itoxi.petnuri.domain.delivery.dto.response.DeliveryListRes;
import com.itoxi.petnuri.domain.delivery.repository.DeliveryAddressRepository;
import com.itoxi.petnuri.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryAddressRepository deliveryAddressRepository;

    public List<DeliveryListRes> getDeliveryAddressList(Member member){
        List<DeliveryListRes> deliveryListResList = deliveryAddressRepository.findAllByMember(member);
        return deliveryListResList;
    }

    @Transactional
    public void deleteDeliveryAddress(Long deliveryAddressId){
        deliveryAddressRepository.deleteById(deliveryAddressId);
    }
}
