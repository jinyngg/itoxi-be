package com.itoxi.petnuri.domain.petTalk.entity;

import static com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus.ACTIVE;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
public class PetTalkPost extends BaseTimeEntity {

    @Id
    @Column(name = "pet_talk_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    
    // TODO 카테고리를 DB에서 관리할 경우, enum으로 작성된 내용 삭제
//    @Column(name = "main_category", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private MainCategory mainCategory;
//
//    @Column(name = "sub_category", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private SubCategory subCategory;

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

    @OneToMany(mappedBy = "petTalkPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<PetTalkPhoto> petTalkPhotos = new ArrayList<>();

    @Transient
    private String thumbnail;

    @Transient
    private boolean liked;

    @Transient
    private Long likeCount;

    @Transient
    private Long replyCount;

    public void uploadThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void uploadPetTalkPhotos(List<PetTalkPhoto> petTalkPhotos) {
        this.petTalkPhotos = petTalkPhotos;
    }

}
