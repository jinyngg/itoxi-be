package com.itoxi.petnuri.domain.petTalk.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itoxi.petnuri.domain.member.dto.Writer;
import com.itoxi.petnuri.domain.petTalk.dto.EmojiCount;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkView;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LoadPetTalkPreviewPostResponse {

    private Long id;
    private String title;
    private String content;

    private String thumbnail;

    private Long viewCount;
    private Long replyCount;

    private boolean reacted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private Long totalEmojiCount;
    private Writer writer;

    public static LoadPetTalkPreviewPostResponse fromEntity(PetTalkView petTalkView) {
        return LoadPetTalkPreviewPostResponse.builder()
                .id(petTalkView.getPetTalkId())
                .title(petTalkView.getTitle())
                .content(petTalkView.getContent())
                .thumbnail(petTalkView.getThumbnail())
                .viewCount(petTalkView.getViewCount())
                .replyCount(petTalkView.getReplyCount())
                .reacted(petTalkView.isReacted())
                .createdAt(petTalkView.getCreatedAt())
                .totalEmojiCount(petTalkView.getTotalEmojiCount())
                .writer(Writer.fromEntity(petTalkView.getWriter()))
                .build();
    }

}
