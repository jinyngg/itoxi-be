package com.itoxi.petnuri.domain.member.service;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.domain.member.dto.LoginResDto;
import com.itoxi.petnuri.domain.member.dto.KakaoInfo;
import com.itoxi.petnuri.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String BEARER_TYPE = "Bearer";

    private final MemberRepository memberRepository;
    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final JwtTokenProvider jwtTokenProvider;


    //프론트에서 전달받은 code로 accessToken 발급 받기
    public OAuth2AccessTokenResponse getAccessToken(String code, ClientRegistration provider) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", provider.getClientId());
        body.add("redirect_uri", provider.getRedirectUri());
        body.add("code", code);
        body.add("client_secret", provider.getClientSecret());

        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OAuth2AccessTokenResponse.class)
                .block();

    }

    //발급 받은 accessToken으로 카카오 사용자 정보 가져오기
    //신규 회원 가입 -> email 반환
    //기존 회원 로그인 -> jwt토큰 반환
    public KakaoInfo getMemberProfile(ClientRegistration provider, OAuth2AccessTokenResponse tokenResponse)  {

        Map<String, Object> userAttributes = WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(String.valueOf(tokenResponse.getAccessToken())))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        KakaoInfo kakaoInfo = new KakaoInfo(userAttributes);

        return kakaoInfo;

    }


    //카카오 이메일로 이미 가입되어 있는 회원인지 확인
    //가입되어 있지 않다면 회원가입에 필요한 정보 프론트에 넘겨주기
    //이미 가입되어 있다면 로그인(자동 로그인?)에 필요한 jwtToken 넘겨주기
    public LoginResDto kakaoLogin(String code)  {

        ClientRegistration provider = inMemoryRepository.findByRegistrationId("kakao");
        OAuth2AccessTokenResponse tokenResponse = getAccessToken(code, provider);

        KakaoInfo kakaoInfo = getMemberProfile(provider, tokenResponse);

        Member member = memberRepository.findByEmail(kakaoInfo.getEmail())
                .orElse(null);

        if(member == null){
            return LoginResDto.builder().email(kakaoInfo.getEmail()).build();
        }

        String jwtToken = jwtTokenProvider.createAccessToken(member);
        String refreshToken = jwtTokenProvider.createRefreshToken(member);

        return LoginResDto.builder().jwtToken(jwtToken).refreshToken(refreshToken).build();
    }

}
