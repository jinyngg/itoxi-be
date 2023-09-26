package com.itoxi.petnuri.domain.delivery.repository;

import com.itoxi.petnuri.domain.delivery.dto.response.DeliveryListRes;
import com.itoxi.petnuri.domain.delivery.entity.DeliveryAddress;
import com.itoxi.petnuri.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

    List<DeliveryListRes> findAllByMember(Member member);
}
