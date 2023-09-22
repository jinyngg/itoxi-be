package com.itoxi.petnuri.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfo {

    private Map<String, Object> attributes;

    public KakaoInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail(){
        return (String) getKakaoAccount().get("email");
    }

    public Map<String, Object> getKakaoAccount(){
        return (Map<String, Object>) attributes.get("kakao_account");
    }
}
