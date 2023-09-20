package com.itoxi.petnuri.domain.petTalk.controller;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkRequest;
import com.itoxi.petnuri.domain.petTalk.dto.response.LoadPetTalkPostsResponse;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.service.PetTalkService;
import com.itoxi.petnuri.domain.petTalk.type.OrderType;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pet-talk")
@RequiredArgsConstructor
public class PetTalkPostController {

    private final PetTalkService petTalkService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> write(
            @RequestPart MultipartFile[] files,
            @RequestPart WritePetTalkRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        petTalkService.write(principalDetails, files, request);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{mainCategory}/{subCategory}")
    public ResponseEntity<?> loadPetTalkPosts(
            @PathVariable Long mainCategory,
            @PathVariable(required = false) Long subCategory,
            @RequestParam(name = "pet") PetType petType,
            @RequestParam(name = "order") OrderType orderType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Page<PetTalk> petTalkPosts = petTalkService.loadPetTalkPosts(
                mainCategory, subCategory, petType, orderType, page, size);

        LoadPetTalkPostsResponse data = LoadPetTalkPostsResponse.fromPage(petTalkPosts);
        return ResponseEntity.ok(data);
    }

}
