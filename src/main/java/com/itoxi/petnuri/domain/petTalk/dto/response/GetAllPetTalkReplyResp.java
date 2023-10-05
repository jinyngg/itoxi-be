package com.itoxi.petnuri.domain.petTalk.dto.response;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkReply;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetAllPetTalkReplyResp {
    private final List<ReplyDTO> replys;

    public GetAllPetTalkReplyResp(List<ReplyDTO> replys) {
        this.replys = replys;
    }

    @Getter
    public static class ReplyDTO {
        private final WriterDTO writer;
        private final Long replyId;
        private final String content;
        private final LocalDateTime createdAt;
        private final TagDTO tag;

        public ReplyDTO(PetTalkReply reply, TagDTO tag) {
            this.writer = new WriterDTO(reply.getWriter());
            this.replyId = reply.getId();
            this.content = reply.getContent();
            this.createdAt = reply.getCreatedAt();
            this.tag = tag;
        }
    }

    @Getter
    public static class WriterDTO {
        private final Long writerId;
        private final String profileImageUrl;
        private final String nickname;

        public WriterDTO(Member member) {
            this.writerId = member.getId();
            this.profileImageUrl = member.getProfileImageUrl();
            this.nickname = member.getNickname();
        }
    }

    @Getter
    public static class TagDTO {
        private final Long taggedMemberId;

        private final String nickname;

        public TagDTO(Member member) {
            this.taggedMemberId = member.getId();
            this.nickname = member.getNickname();
        }
    }
}