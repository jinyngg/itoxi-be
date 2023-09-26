package com.itoxi.petnuri.domain.delivery.entity;

import com.itoxi.petnuri.domain.delivery.dto.ChallengeDeliveryDTO;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.product.entity.ChallengeProduct;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_address")
public class DeliveryAddress extends BaseTimeEntity {
    @Id
    @Column(name = "delivery_address_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @Column(name = "is_based", nullable = false)
    private Boolean isBased;

    public static DeliveryAddress create(Member member, String name, String phone
            , String roadAddress, String address, String zipcode, Boolean isBased) {
        return DeliveryAddress.builder()
                .member(member)
                .name(name)
                .phone(phone)
                .roadAddress(roadAddress)
                .address(address)
                .zipcode(zipcode)
                .isBased(isBased)
                .build();
    }

    public void updateIsBased(Boolean isBased) {
        this.isBased = isBased;
    }

    public void updateAddress(
            String name, String phone, String roadAddress, String address, String zipcode, Boolean isBased) {
        this.name = name;
        this.phone = phone;
        this.roadAddress = roadAddress;
        this.address = address;
        this.zipcode = zipcode;
        this.isBased = isBased;
    }
}
