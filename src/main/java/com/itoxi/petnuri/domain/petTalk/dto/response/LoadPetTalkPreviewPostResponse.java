package com.itoxi.petnuri.domain.petTalk.dto.response;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadPetTalkPreviewPostResponse {

    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    // Member Writer Dto 생성
//    private Writer writer;
    private String thumbnail;
    // 이모지
//    private Long likeCount;
    private Long replyCount;

    public static LoadPetTalkPreviewPostResponse fromEntity(PetTalk petTalk) {
        return LoadPetTalkPreviewPostResponse.builder()
                .id(petTalk.getId())
                .title(petTalk.getTitle())
                .content(petTalk.getContent())
                .viewCount(petTalk.getViewCount())
//                .writer(petTalkPost.getWriter())
                .thumbnail(petTalk.getThumbnail())
//                .likeCount(petTalkPost.getLikeCount())
                .replyCount(petTalk.getReplyCount())
                .build();
    }

}
