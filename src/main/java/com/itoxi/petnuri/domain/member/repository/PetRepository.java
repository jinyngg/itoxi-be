package com.itoxi.petnuri.domain.member.repository;

import com.itoxi.petnuri.domain.member.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
