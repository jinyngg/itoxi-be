package com.itoxi.petnuri.domain.petTalk.dto.response;

import com.itoxi.petnuri.domain.member.dto.Writer;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadPetTalkPreviewPostResponse {

    private Long id;
    private String title;
    private String content;

    private String thumbnail;

    private Long viewCount;
    private Long emojiCount;
    private Long replyCount;

    private boolean reacted;

    private Writer writer;

    public static LoadPetTalkPreviewPostResponse fromEntity(PetTalk petTalk) {
        return LoadPetTalkPreviewPostResponse.builder()
                .id(petTalk.getId())
                .title(petTalk.getTitle())
                .content(petTalk.getContent())
                .thumbnail(petTalk.getThumbnail())
                .viewCount(petTalk.getViewCount())
                .emojiCount(petTalk.getEmojiCount())
                .replyCount(petTalk.getReplyCount())
                .reacted(petTalk.isReacted())
                .writer(Writer.fromEntity(petTalk.getWriter()))
                .build();
    }

}
