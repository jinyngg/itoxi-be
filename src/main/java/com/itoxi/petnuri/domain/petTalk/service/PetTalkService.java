package com.itoxi.petnuri.domain.petTalk.service;

import com.itoxi.petnuri.domain.petTalk.dto.request.WritePetTalkRequest;
import org.springframework.security.core.Authentication;

public interface PetTalkService {

    void write(Authentication authentication, WritePetTalkRequest request);
}
