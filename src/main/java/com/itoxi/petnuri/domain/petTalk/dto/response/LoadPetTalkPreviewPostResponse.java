package com.itoxi.petnuri.domain.petTalk.dto.response;

import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPost;
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

    public static LoadPetTalkPreviewPostResponse fromEntity(PetTalkPost petTalkPost) {
        return LoadPetTalkPreviewPostResponse.builder()
                .id(petTalkPost.getId())
                .title(petTalkPost.getTitle())
                .content(petTalkPost.getContent())
                .viewCount(petTalkPost.getViewCount())
//                .writer(petTalkPost.getWriter())
                .thumbnail(petTalkPost.getThumbnail())
//                .likeCount(petTalkPost.getLikeCount())
                .replyCount(petTalkPost.getReplyCount())
                .build();
    }

}
