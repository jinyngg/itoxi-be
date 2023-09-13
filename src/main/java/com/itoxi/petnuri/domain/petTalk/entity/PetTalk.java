package com.itoxi.petnuri.domain.petTalk.entity;

import static com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus.ACTIVE;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.type.MainCategory;
import com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.domain.petTalk.type.SubCategory;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pet_talk")
public class PetTalk extends BaseTimeEntity {

    @Id
    @Column(name = "pet_talk_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "main_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private MainCategory mainCategory;

    @Column(name = "sub_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;

    @Column(name = "pet_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Builder.Default
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PetTalkStatus status = ACTIVE;

    @Builder.Default
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @Transient
    private String thumbnail;

    @Transient
    private boolean liked;

    @Transient
    private Long likeCount;

    @Transient
    private Long replyCount;

}
