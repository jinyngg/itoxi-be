package com.itoxi.petnuri.domain.delivery.repository;

import com.itoxi.petnuri.domain.delivery.entity.DeliveryAddress;
import com.itoxi.petnuri.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    Optional<DeliveryAddress> findByMember(Member member);

    List<DeliveryAddress> findAllByMember(Member member);
}
