package com.itoxi.petnuri.domain.petTalk.service;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkReplyReq;
import com.itoxi.petnuri.domain.petTalk.dto.response.GetAllPetTalkReplyResp;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkReply;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkReplyRepository;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetTalkReplyService {
    private final PetTalkRepository petTalkRepository;
    private final PetTalkReplyRepository petTalkReplyRepository;

    @Transactional
    public void write(Member writer, Long petTalkId, WritePetTalkReplyReq request) {
        PetTalk petTalk = petTalkRepository.getById(petTalkId);
        PetTalkReply petTalkReply = PetTalkReply.create(writer, petTalk, request.getContent());

        // 자식 댓글인 경우 부모 update
        if (request.getParentId() != null) {
            PetTalkReply parent = petTalkReplyRepository.findById(request.getParentId())
                    .orElseThrow(() -> new Exception400(ErrorCode.REPLY_NOT_FOUND));

            // 같은 게시글에 대한 댓글인지 체크
            if (!parent.getPetTalk().equals(petTalkReply.getPetTalk())) {
                throw new Exception400(ErrorCode.MISMATCH_PET_TALK_ID);
            }

            petTalkReply.updateParent(parent);
        }

        petTalkReplyRepository.save(petTalkReply);
        petTalkRepository.addReplyCount(petTalk);
    }

    @Transactional(readOnly = true)
    public GetAllPetTalkReplyResp getAll(Long petTalkId) {
        List<PetTalkReply> replyList = petTalkReplyRepository.findAllByPetTalkId(petTalkId);

        List<GetAllPetTalkReplyResp.ReplyDTO> ReplyDTOList = replyList.stream()
                .map(reply -> {
                    PetTalkReply parent = reply.getParent();
                    GetAllPetTalkReplyResp.TagDTO tag = (parent == null) ? null : new GetAllPetTalkReplyResp.TagDTO(parent.getWriter());
                    return new GetAllPetTalkReplyResp.ReplyDTO(reply, tag);
                })
                .collect(Collectors.toList());

        return new GetAllPetTalkReplyResp(ReplyDTOList);
    }
}