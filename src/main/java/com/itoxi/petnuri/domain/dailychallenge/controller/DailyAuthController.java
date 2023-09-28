package com.itoxi.petnuri.domain.dailychallenge.controller;

import com.itoxi.petnuri.domain.dailychallenge.dto.DailyAuthDto;
import com.itoxi.petnuri.domain.dailychallenge.dto.response.DailyAuthResponse;
import com.itoxi.petnuri.domain.dailychallenge.service.DailyAuthService;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.point.service.PointService;
import com.itoxi.petnuri.global.common.customValid.valid.ValidId;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Consumer;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.INVALID_FILE_REQUEST;

/**
 * author         : matrix
 * date           : 2023-09-18
 * description    :
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge/daily")
public class DailyAuthController {

    private final DailyAuthService dailyAuthService;
    private final PointService pointService;

    @PostMapping(path ="/{challengeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DailyAuthResponse> saveAuth(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable @ValidId Long challengeId,
            @RequestPart MultipartFile file) {
        
        isNotEmptyFile(file); // 업로드한 파일의 유효성 검사
        Member member = principalDetails.getMember();

        // 1. 인증 글 등록 후 포인트 적립을 위한 데이터 받아 오기
        DailyAuthDto dailyAuthDto = dailyAuthService.saveAuth(member, challengeId, file);

        // 2. 포인트 적립 후 현재 회원의 보유 포인트 받아 오기
        Long point = pointService.getPoint(dailyAuthDto, member.getId());

        DailyAuthResponse dailyAuthResponse = DailyAuthResponse.of(dailyAuthDto, member, point);
        return new ResponseEntity<>(dailyAuthResponse, HttpStatus.CREATED); // 201 OK
    }

    private void isNotEmptyFile(MultipartFile file) {
        if (file.isEmpty() || file == null) {
            throw new Exception400(INVALID_FILE_REQUEST);
        }
    }
}
