package com.itoxi.petnuri.domain.member.controller;

import com.itoxi.petnuri.domain.member.dto.request.PetProfileReq;
import com.itoxi.petnuri.domain.member.dto.request.PetSaveReq;
import com.itoxi.petnuri.domain.member.dto.request.ProfileUpdateReq;
import com.itoxi.petnuri.domain.member.dto.response.MainResp;
import com.itoxi.petnuri.domain.member.dto.response.MyPageResp;
import com.itoxi.petnuri.domain.member.dto.response.ProfileUpdateResp;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.service.MemberService;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import com.itoxi.petnuri.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/mypage")
    public ResponseEntity<MyPageResp> myPage(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Member member = principalDetails.getMember();
        MyPageResp response = memberService.getMyPage(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/mypage/profile")
    public ResponseEntity<ProfileUpdateResp> profileUpdate(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestPart(value = "nickname") @Valid ProfileUpdateReq request,
            @RequestPart(value = "file", required = false)MultipartFile file
    ) {
        Member member = principalDetails.getMember();
        ProfileUpdateResp response = memberService.updateProfile(member, request, file);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/pet")
    public ResponseEntity savePet(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody PetSaveReq petSaveReq){
        memberService.savePet(principalDetails.getMember(), petSaveReq);

        return new ResponseEntity("펫 정보 등록 완료", HttpStatus.CREATED);
    }

    @DeleteMapping("/mypage/withdraw")
    public ResponseEntity<String> withdraw(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestHeader(JwtTokenProvider.HEADER) String accessToken
    ) {
        Member member = principalDetails.getMember();
        accessToken = jwtTokenProvider.resolveToken(accessToken);
        memberService.withdraw(member, accessToken);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader(JwtTokenProvider.HEADER) String accessToken){
        memberService.logout(accessToken);

        return new ResponseEntity("서비스 로그아웃 완료", HttpStatus.OK);

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/main/pet/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity addPet(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                 @RequestPart(value = "petProfileReq") PetProfileReq petProfileReq, @RequestPart(value = "file") MultipartFile image){

        memberService.savePet(principalDetails.getMember(), petProfileReq, image);

        return new ResponseEntity("펫 프로필 추가 성공", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(value = "/main/pet/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity updatePet(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                    @RequestPart(value = "petProfileReq") PetProfileReq petProfileReq, @RequestPart(value = "file") MultipartFile image){

        memberService.updatePet(principalDetails.getMember(), petProfileReq, image);

        return new ResponseEntity("펫 프로필 수정 성공", HttpStatus.OK);
    }

    @GetMapping("/main")
    public ResponseEntity<MainResp> mainHome(
            Authentication authentication
    ) {
        MainResp response = memberService.getMain(authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
