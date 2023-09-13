package com.itoxi.petnuri.domain.petTalk.controller;

import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkRequest;
import com.itoxi.petnuri.domain.petTalk.service.PetTalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet-talk")
@RequiredArgsConstructor
public class PetTalkController {

    private final PetTalkService petTalkService;

    @PostMapping
    public ResponseEntity<Object> write(Authentication authentication, @RequestBody WritePetTalkRequest request) {
        petTalkService.write(authentication, request);
        return ResponseEntity.ok(null);
    }
}
