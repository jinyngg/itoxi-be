package com.itoxi.petnuri.domain.eventChallenge.controller;

import com.itoxi.petnuri.domain.eventChallenge.dto.request.WritePointChallengePostRequest;
import com.itoxi.petnuri.domain.eventChallenge.service.PointChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/challenge/point")
@RequiredArgsConstructor
public class PointChallengeAdminController {

    private final PointChallengeService pointChallengeService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> writePointChallengePost(
            @RequestPart MultipartFile thumbnail,
            @RequestPart MultipartFile poster,
            @RequestPart WritePointChallengePostRequest request) {
        pointChallengeService.write(thumbnail, poster, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

}
