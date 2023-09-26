package com.itoxi.petnuri.domain.petTalk.repository;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PetTalkJpaRepository extends JpaRepository<PetTalk, Long>, PetTalkJpaRepositoryCustom {
    @Query("SELECT pt, COUNT(pe) AS emojiCount " +
            "FROM PetTalk pt " +
            "LEFT JOIN PetTalkEmotion pe ON pt.id = pe.petTalk.id " +
            "WHERE pt.createdAt >= :oneWeekAgo " +
            "GROUP BY pt.id " +
            "ORDER BY emojiCount DESC, pt.createdAt DESC")
    List<PetTalk> findTopPetTalksOrderByRanking(@Param("oneWeekAgo") LocalDateTime oneWeekAgo, Pageable pageable);
}
