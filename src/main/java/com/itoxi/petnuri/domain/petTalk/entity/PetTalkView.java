package com.itoxi.petnuri.domain.petTalk.entity;

import static com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus.ACTIVE;

import com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet_talk_view")
@Entity
@Immutable
public class PetTalkView {

    @Id
    @Column(name = "pet_talk_id")
    private Long petTalkId;

    @Column(name = "main_category_id")
    private Long mainCategoryId;

    @Column(name = "sub_category_id")
    private Long subCategoryId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "pet_type")
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PetTalkStatus status;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "cute_count")
    private Long cuteCount;

    @Column(name = "fun_count")
    private Long funCount;

    @Column(name = "kiss_count")
    private Long kissCount;

    @Column(name = "omg_count")
    private Long omgCount;

    @Column(name = "sad_count")
    private Long sadCount;

    @Column(name = "score")
    private Double score;

    @Column(name = "total_emoji_count")
    private Long totalEmojiCount;
}
