package com.itoxi.petnuri.domain.petTalk.dto.response;

import com.itoxi.petnuri.domain.member.dto.Writer;
import com.itoxi.petnuri.domain.petTalk.dto.PetTalkPhotoDTO;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadPetTalkPostDetailsResponse {

    private List<PetTalkPhotoDTO> petTalkPhotos;

    private Long id;
    private String title;
    private String content;

    private Long viewCount;
    private Long emojiCount;
    private Long replyCount;

    private boolean reacted;

    private Writer writer;

    public static LoadPetTalkPostDetailsResponse fromEntity(PetTalk petTalk) {
        return LoadPetTalkPostDetailsResponse.builder()
                .petTalkPhotos(petTalk.getPetTalkPhotos().stream()
                        .map(PetTalkPhotoDTO::fromEntity)
                        .collect(Collectors.toList()))
                .id(petTalk.getId())
                .title(petTalk.getTitle())
                .content(petTalk.getContent())
                .viewCount(petTalk.getViewCount())
                .emojiCount(petTalk.getEmojiCount())
                .replyCount(petTalk.getReplyCount())
                .reacted(petTalk.isReacted())
                .writer(Writer.fromEntity(petTalk.getWriter()))
                .build();
    }

}
