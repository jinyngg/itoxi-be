package com.itoxi.petnuri.domain.petTalk.entity;

import com.itoxi.petnuri.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "pet_talk_reply")
@Entity
public class PetTalkReply extends BaseTimeEntity {
    @Id
    @Column(name = "pet_talk_reply_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_talk_id")
    private PetTalk petTalk;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PetTalkReply parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<PetTalkReply> children = new ArrayList<>();

}
