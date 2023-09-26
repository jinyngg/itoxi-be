package com.itoxi.petnuri.domain.member.repository;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByMember(Member member);
}
