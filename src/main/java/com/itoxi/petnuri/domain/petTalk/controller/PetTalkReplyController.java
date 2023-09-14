package com.itoxi.petnuri.domain.petTalk.controller;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkReplyReq;
import com.itoxi.petnuri.domain.petTalk.service.PetTalkReplyService;
import com.itoxi.petnuri.global.common.customValid.valid.ValidId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet-talk")
@RequiredArgsConstructor
@Validated
public class PetTalkReplyController {
    private final PetTalkReplyService petTalkReplyService;

    @PostMapping("/{petTalkId}/reply")
    public ResponseEntity<Object> write(
            @PathVariable @ValidId Long petTalkId,
            @RequestBody WritePetTalkReplyReq request,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Member member = principalDetails.getMember();
        petTalkReplyService.write(member, petTalkId, request);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}