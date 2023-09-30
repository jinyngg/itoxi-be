package com.itoxi.petnuri.domain.dailychallenge.controller;

import com.itoxi.petnuri.domain.dailychallenge.dto.request.DailyChallengeRequest;
import com.itoxi.petnuri.domain.dailychallenge.service.DailyChallengeService;
import com.itoxi.petnuri.global.common.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.INVALID_FILE_REQUEST;

/**
 * author         : Jisang Lee
 * date           : 2023-09-30
 * description    :
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/challenge/daily")
public class DailyChallengeAdminController {

    private final DailyChallengeService dailyChallengeService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> registrationChallenge(
            @RequestPart MultipartFile thumbnail,
            @RequestPart MultipartFile banner,
            @RequestPart DailyChallengeRequest request
    ) {
        isNotEmptyFile(thumbnail);
        isNotEmptyFile(banner);

        dailyChallengeService.registerDailyChallenge(request, thumbnail, banner);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    private void isNotEmptyFile(MultipartFile file) {
        if (file.isEmpty() || file == null) {
            throw new Exception400(INVALID_FILE_REQUEST);
        }
    }
}
