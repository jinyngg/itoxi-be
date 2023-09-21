package com.itoxi.petnuri.domain.member.service;

import com.itoxi.petnuri.domain.member.dto.response.KakaoInfo;
import com.itoxi.petnuri.domain.member.dto.response.KakaoToken;
import com.itoxi.petnuri.domain.member.dto.response.LoginRes;
import com.itoxi.petnuri.domain.member.dto.request.JoinReq;
import com.itoxi.petnuri.domain.member.dto.response.*;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.domain.point.entity.Point;
import com.itoxi.petnuri.domain.point.entity.PointHistory;
import com.itoxi.petnuri.domain.point.repository.PointHistoryRepository;
import com.itoxi.petnuri.domain.point.repository.PointRepository;
import com.itoxi.petnuri.global.common.exception.Exception400;
import com.itoxi.petnuri.global.common.exception.Exception404;
import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import com.itoxi.petnuri.global.redis.RedisService;
import com.itoxi.petnuri.global.security.jwt.JwtTokenProvider;
import com.itoxi.petnuri.global.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Set<String> existingCodes = new HashSet<>();
    private static final String UPPER_CASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final Long REFERRAL_POINT = 2000L;

    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final JsonConverter jsonConverter;

    //프론트에서 전달받은 code로 accessToken 발급 받기
    public KakaoToken getAccessToken(String code, ClientRegistration provider) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", provider.getClientId());
        body.add("redirect_uri", provider.getRedirectUri());
        body.add("code", code);
        body.add("client_secret", provider.getClientSecret());

        WebClient wc = WebClient.create(provider.getProviderDetails().getTokenUri());
        String response = wc.post()
                .uri(provider.getProviderDetails().getTokenUri())
                .body(BodyInserters.fromFormData(body))
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        KakaoToken kakaoToken = jsonConverter.jsonToObject(response, KakaoToken.class);

        return kakaoToken;
    }

    //발급 받은 accessToken으로 카카오 사용자 정보 가져오기
    //신규 회원 가입 -> email 반환
    //기존 회원 로그인 -> jwt토큰 반환
    public KakaoInfo getMemberProfile(ClientRegistration provider, KakaoToken kakaoToken)  {

        System.out.println("카카오 토큰 : " + kakaoToken.getAccessToken());

        Map<String, Object> userAttributes = WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(String.valueOf(kakaoToken.getAccessToken())))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        KakaoInfo kakaoInfo = new KakaoInfo(userAttributes);

        return kakaoInfo;
    }

    //카카오 이메일로 이미 가입되어 있는 회원인지 확인
    //가입되어 있지 않다면 회원가입에 필요한 정보 프론트에 넘겨주기
    //이미 가입되어 있다면 로그인(자동 로그인?)에 필요한 jwtToken 넘겨주기
    public LoginRes kakaoLogin(String code)  {

        ClientRegistration provider = inMemoryRepository.findByRegistrationId("kakao");
        KakaoToken tokenResponse = getAccessToken(code, provider);

        KakaoInfo kakaoInfo = getMemberProfile(provider, tokenResponse);

        Member member = memberRepository.findByEmail(kakaoInfo.getEmail())
                .orElse(null);

        if(member == null){
            return LoginRes.builder().email(kakaoInfo.getEmail()).build();
        }

        String jwtToken = jwtTokenProvider.createAccessToken(member);

        return LoginRes.builder().jwtToken(jwtToken).email(kakaoInfo.getEmail()).build();
    }

    @Transactional
    public JoinResp join(JoinReq request) {
        String email = request.getEmail();
        String nickname = request.getNickname();
        String invitedCode = request.getReferralCode();

        if (memberRepository.existsByEmail(email)) {
            throw new Exception400(ErrorCode.DUPLICATED_EMAIL);
        }

        if (memberRepository.existsByNickname(nickname)) {
            throw new Exception400(ErrorCode.DUPLICATED_NICKNAME);
        }

        String referralCode = generateRandomCode();
        Member joinMember = Member.createMember(email, nickname, referralCode);
        memberRepository.save(joinMember);

        Point pointByJoin = Point.createPoint(joinMember);
        pointRepository.save(pointByJoin);

        // 추천인 혜택
        if (invitedCode != null && !invitedCode.isEmpty()) {
            referralBenefit(invitedCode);
            joinBenefit(pointByJoin);
        }

        // 논의필요
        String jwtToken = jwtTokenProvider.createAccessToken(joinMember);
        String refreshToken = jwtTokenProvider.createRefreshToken(joinMember);

        redisService.setObjectByKey(RedisService.REFRESH_TOKEN_PREFIX + joinMember.getEmail(), refreshToken,
                JwtTokenProvider.EXP_REFRESH, TimeUnit.MILLISECONDS);

        return new JoinResp(joinMember, jwtToken, refreshToken);
    }

    private void referralBenefit(String invitedCode) {
        Member referralMember = memberRepository.findByReferralCode(invitedCode)
                .orElseThrow(() -> new Exception404(ErrorCode.REFERRAL_CODE_NOT_FOUND));

        Point pointByReferral = pointRepository.findByMember(referralMember)
                .orElseThrow(() -> new Exception404(ErrorCode.POINT_NOT_FOUND));

        PointHistory referralHistory = PointHistory.createGetPointHistory(pointByReferral, REFERRAL_POINT, "추천인 가입");
        pointHistoryRepository.save(referralHistory);
    }

    private void joinBenefit(Point pointByJoin) {
        PointHistory joinHistory = PointHistory.createGetPointHistory(pointByJoin, REFERRAL_POINT, "추천인 입력");
        pointHistoryRepository.save(joinHistory);
    }

    @Transactional(readOnly = true)
    public ExistEmailResp checkEmail(String email) {
        Boolean isExists = memberRepository.existsByEmail(email);
        return new ExistEmailResp(email, isExists);
    }

    @Transactional(readOnly = true)
    public ExistReferralCodeResp checkReferralCode(String referralCode) {
        Boolean isExists = memberRepository.existsByReferralCode(referralCode);
        return new ExistReferralCodeResp(referralCode, isExists);
    }

    private String generateRandomCode() {
        List<Character> characters = new ArrayList<>();
        Random random = new Random();
        String uniqueCode;

        do {
            for (int i = 0; i < 5; i++) {
                int randomIndex = random.nextInt(UPPER_CASE_CHARACTERS.length());
                char randomChar = UPPER_CASE_CHARACTERS.charAt(randomIndex);
                characters.add(randomChar);
            }

            for (int i = 0; i < 3; i++) {
                int randomIndex = random.nextInt(DIGITS.length());
                char randomChar = DIGITS.charAt(randomIndex);
                characters.add(randomChar);
            }

            Collections.shuffle(characters);

            StringBuilder codeBuilder = new StringBuilder(characters.size());
            for (Character character : characters) {
                codeBuilder.append(character);
            }

            uniqueCode = codeBuilder.toString();
        } while (existingCodes.contains(uniqueCode));

        existingCodes.add(uniqueCode);

        return uniqueCode;
    }

    public ReissueResp reissue(String accessToken, String refreshToken) {
        jwtTokenProvider.isTokenValid(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String redisRT = redisService.getObjectByKey(RedisService.REFRESH_TOKEN_PREFIX
                + authentication.getName(), String.class);

        if (!redisRT.equals(refreshToken)) {
            throw new Exception400(ErrorCode.NOT_MATCH_TOKEN);
        }

        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new Exception400(ErrorCode.USER_NOT_FOUND));

        String newAT = jwtTokenProvider.createAccessToken(member);
        String newRT = jwtTokenProvider.createRefreshToken(member);

        redisService.setObjectByKey(RedisService.REFRESH_TOKEN_PREFIX + member.getEmail(), newRT,
                JwtTokenProvider.EXP_REFRESH, TimeUnit.MILLISECONDS);

        return new ReissueResp(newAT, newRT);
    }
}
