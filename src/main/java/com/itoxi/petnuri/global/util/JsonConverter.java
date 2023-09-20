package com.itoxi.petnuri.global.util;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.JSON_PARSE_FAILED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itoxi.petnuri.global.common.exception.Exception500;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class JsonConverter {
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public String objectToJson(Object obj) {
        String jsonStr = "";
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception500(JSON_PARSE_FAILED);
        }
        return jsonStr;
    }

    public <T> T jsonToObject(String json, Class<T> clazz) {
        T obj = null;
        try {
            obj = objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception500(JSON_PARSE_FAILED);
        }
        return obj;
    }
}
