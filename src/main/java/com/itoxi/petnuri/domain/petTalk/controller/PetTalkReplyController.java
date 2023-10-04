package com.itoxi.petnuri.domain.petTalk.controller;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkReplyReq;
import com.itoxi.petnuri.domain.petTalk.dto.response.GetAllPetTalkReplyResp;
import com.itoxi.petnuri.domain.petTalk.service.PetTalkReplyService;
import com.itoxi.petnuri.global.common.customValid.valid.ValidId;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/pet-talk")
@RequiredArgsConstructor
@Validated
public class PetTalkReplyController {
    private final PetTalkReplyService petTalkReplyService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{petTalkId}/reply")
    public ResponseEntity<Object> write(
            @PathVariable @ValidId Long petTalkId,
            @RequestBody @Valid WritePetTalkReplyReq request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Errors errors
    ) {
        Member member = principalDetails.getMember();
        petTalkReplyService.write(member, petTalkId, request);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("/{petTalkId}/replys")
    public ResponseEntity<GetAllPetTalkReplyResp> getAll(
            @PathVariable @ValidId Long petTalkId
    ) {
        GetAllPetTalkReplyResp response = petTalkReplyService.getAll(petTalkId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}