package com.itoxi.petnuri.domain.petTalk.entity;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.type.EmojiType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet_talk_emotion")
@Entity
public class PetTalkEmotion {

    @Id
    @Column(name = "pet_talk_emotion_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_talk_id")
    private PetTalk petTalk;

    @Column(name = "emoji", nullable = false)
    @Enumerated(EnumType.STRING)
    EmojiType emoji;

    public static PetTalkEmotion create(Member member, PetTalk petTalk, EmojiType emoji) {
        return PetTalkEmotion.builder()
                .member(member)
                .petTalk(petTalk)
                .emoji(emoji)
                .build();
    }

}
