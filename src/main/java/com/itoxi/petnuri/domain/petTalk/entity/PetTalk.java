package com.itoxi.petnuri.domain.petTalk.entity;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.itoxi.petnuri.domain.petTalk.type.PetTalkStatus.ACTIVE;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member writer;

    @OneToMany(mappedBy = "petTalk", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<PetTalkPhoto> petTalkPhotos = new ArrayList<>();

    @Transient
    private String thumbnail;

    @Transient
    private boolean reacted; // 로그인된 사용자 이모지 반응 boolean 값

    @Transient
    private Long emojiCount = 0L;

    @Transient
    private Long replyCount = 0L;

    public static PetTalk createByChallengeReview(
            Member member, String challengeTitle, String content, MainCategory mainCategory, PetType petType
    ) {
        String title = member.getNickname() + " 님의 " + challengeTitle + " 챌린지 인증입니다";
        return PetTalk.builder()
                .title(title)
                .content(content)
                .mainCategory(mainCategory)
                .petType(petType)
                .writer(member)
                .build();
    }

    public void react(boolean reacted) {
        this.reacted = reacted;
    }

    public PetTalk addEmojiCount() {
        this.emojiCount += 1L;
        return this;
    }

    public PetTalk subtractEmojiCount() {
        this.emojiCount -= 1L;
        return this;
    }

    public PetTalk addReplyCount() {
        this.replyCount += 1L;
        return this;
    }

    public void updateStatus(PetTalkStatus status) {
        this.status = status;
    }

    public void uploadThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void uploadPetTalkPhotos(List<PetTalkPhoto> petTalkPhotos) {
        this.petTalkPhotos = petTalkPhotos;
    }

}
