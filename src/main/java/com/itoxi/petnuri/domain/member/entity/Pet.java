package com.itoxi.petnuri.domain.member.entity;

import com.itoxi.petnuri.domain.member.dto.request.PetProfileReq;
import com.itoxi.petnuri.domain.petTalk.type.PetGender;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Enumerated(EnumType.STRING)
    private PetType species;

    @Column(nullable = false)
    private String petName;

    private String image;

    private String breed;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetGender petGender;

    @Column(nullable = false)
    private Integer petAge;

    private Boolean isSelected;

    public void updateIsSelected(Boolean isSelected){
        this.isSelected = isSelected;
    }

    public void updatePet(Member member, PetProfileReq petProfileReq, String originUrl){
        this.member = member;
        this.petAge = petProfileReq.getPetAge();
        this.petGender = PetGender.stringToPetGender(petProfileReq.getPetGender());
        this.petName = petProfileReq.getPetName();
        this.isSelected = petProfileReq.getIsSelected();
        this.image = originUrl;
    }

}
