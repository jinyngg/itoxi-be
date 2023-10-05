package com.itoxi.petnuri.domain.petTalk.controller;

import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkRequest;
import com.itoxi.petnuri.domain.petTalk.dto.response.LoadPetTalkPostDetailsResponse;
import com.itoxi.petnuri.domain.petTalk.dto.response.LoadPetTalkPostsResponse;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkView;
import com.itoxi.petnuri.domain.petTalk.service.PetTalkService;
import com.itoxi.petnuri.domain.petTalk.type.OrderType;
import com.itoxi.petnuri.domain.petTalk.type.PetType;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pet-talk")
@RequiredArgsConstructor
public class PetTalkPostController {

    private final PetTalkService petTalkService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> write(
            @RequestPart(required = false) MultipartFile[] files,
            @RequestPart WritePetTalkRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        petTalkService.write(principalDetails, files, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping()
    public ResponseEntity<LoadPetTalkPostsResponse> loadPetTalkPosts(
            @RequestParam(required = false) Long mainCategory,
            @RequestParam(required = false) Long subCategory,
            @RequestParam(name = "pet") PetType petType,
            @RequestParam(name = "order") OrderType orderType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            Authentication authentication) {
        Page<PetTalkView> petTalkPosts = petTalkService.loadPetTalkPosts(
                authentication, mainCategory, subCategory, petType, orderType, page, size);

        LoadPetTalkPostsResponse data =
                LoadPetTalkPostsResponse.fromPage(petTalkPosts);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{petTalkId}")
    public ResponseEntity<LoadPetTalkPostDetailsResponse> loadPetTalkPostDetails(
            @PathVariable Long petTalkId,
            Authentication authentication) {
        PetTalkView petTalkView = petTalkService.loadPetTalkPostDetails(authentication, petTalkId);
        LoadPetTalkPostDetailsResponse data =
                LoadPetTalkPostDetailsResponse.fromEntity(petTalkView);
        return ResponseEntity.ok(data);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/{petTalkId}")
    public ResponseEntity<Object> deletePetTalkPost(
            @PathVariable Long petTalkId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        petTalkService.deletePetTalkPost(principalDetails, petTalkId);
        return ResponseEntity.ok(null);
    }

}
