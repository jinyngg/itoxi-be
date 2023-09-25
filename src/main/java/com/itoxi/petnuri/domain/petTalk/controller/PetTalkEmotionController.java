package com.itoxi.petnuri.domain.petTalk.controller;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.CreatePetTalkEmotionReq;
import com.itoxi.petnuri.domain.petTalk.service.PetTalkEmotionService;
import com.itoxi.petnuri.domain.petTalk.type.EmojiType;
import com.itoxi.petnuri.global.common.customValid.valid.ValidEnum;
import com.itoxi.petnuri.global.common.customValid.valid.ValidId;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/pet-talk")
@RequiredArgsConstructor
public class PetTalkEmotionController {
    private final PetTalkEmotionService petTalkEmotionService;

    @PostMapping("/{petTalkId}/emotion")
    public ResponseEntity<Object> create(
            @PathVariable @ValidId Long petTalkId,
            @RequestBody @Valid CreatePetTalkEmotionReq request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Errors errors
    ) {
        Member member = principalDetails.getMember();
        petTalkEmotionService.create(member, petTalkId, request);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{petTalkId}/emotion")
    public ResponseEntity<Object> delete(
            @PathVariable @ValidId Long petTalkId,
            @RequestParam @ValidEnum(enumClass = EmojiType.class) EmojiType emoji,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Member member = principalDetails.getMember();
        petTalkEmotionService.delete(member, petTalkId, emoji);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}