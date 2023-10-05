package com.itoxi.petnuri.domain.petTalk.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itoxi.petnuri.domain.member.dto.Writer;
import com.itoxi.petnuri.domain.petTalk.dto.EmojiCount;
import com.itoxi.petnuri.domain.petTalk.dto.PetTalkPhotoDTO;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkView;
import java.time.LocalDateTime;
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
    private Long replyCount;

    private boolean reacted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private EmojiCount emoji;
    private Writer writer;

    public static LoadPetTalkPostDetailsResponse fromEntity(PetTalkView petTalkView) {
        return LoadPetTalkPostDetailsResponse.builder()
                .petTalkPhotos(petTalkView.getPetTalkPhotos().stream()
                        .map(PetTalkPhotoDTO::fromEntity)
                        .collect(Collectors.toList()))
                .id(petTalkView.getPetTalkId())
                .title(petTalkView.getTitle())
                .content(petTalkView.getContent())
                .viewCount(petTalkView.getViewCount())
                .replyCount(petTalkView.getReplyCount())
                .reacted(petTalkView.isReacted())
                .createdAt(petTalkView.getCreatedAt())
                .emoji(EmojiCount.fromEntity(petTalkView))
                .writer(Writer.fromEntity(petTalkView.getWriter()))
                .build();
    }

}
