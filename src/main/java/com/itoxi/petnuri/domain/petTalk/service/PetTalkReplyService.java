package com.itoxi.petnuri.domain.petTalk.service;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkReplyReq;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkReply;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkReplyRepository;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetTalkReplyService {
    private final PetTalkRepository petTalkRepository;
    private final PetTalkReplyRepository petTalkReplyRepository;

    public void write(Member writer, Long petTalkId, WritePetTalkReplyReq request) {
        PetTalk petTalk = petTalkRepository.findById(petTalkId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다"));
        PetTalkReply petTalkReply = PetTalkReply.create(writer, petTalk, request.getContent());

        // 자식 댓글인 경우 부모 update
        if (request.getParentId() != null) {
            PetTalkReply parent = petTalkReplyRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다"));

            // 같은 게시글에 대한 댓글인지 체크
            if (!parent.getPetTalk().equals(petTalkReply.getPetTalk())) {
                throw new RuntimeException("게시글 id가 다릅니다");
            }

            petTalkReply.updateParent(parent);
        }

        petTalkReplyRepository.save(petTalkReply);
    }
}