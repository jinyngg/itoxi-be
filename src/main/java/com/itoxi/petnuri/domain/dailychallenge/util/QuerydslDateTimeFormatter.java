package com.itoxi.petnuri.domain.dailychallenge.util;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * author         : Jisang Lee
 * date           : 2023-09-26
 * description    :
 */
@Component
public class QuerydslDateTimeFormatter {
    // Todo: 서버 배포시 application.yml에서 profiles 값을 읽어 오도록 코드 변경 (test, dev, prod)
    @Value("${spring.datasource.driver-class-name}")
    private String dbName;

    public StringExpression formatter(DateTimePath<LocalDateTime> reqDateTime) {
        if (dbName.contains("mysql")) {
            return getMySqlDate(reqDateTime);
        }
        return getH2Date(reqDateTime);
    }

    private StringExpression getH2Date(DateTimePath<LocalDateTime> reqDateTime) {
        return Expressions.stringTemplate("FUNCTION('FORMATDATETIME', {0}, 'yyyy-MM-dd')", reqDateTime);
    }

    private StringExpression getMySqlDate(DateTimePath<LocalDateTime> reqDateTime) {
        return Expressions.stringTemplate("FUNCTION('DATE_FORMAT', {0}, '%Y-%m-%d')", reqDateTime);
    }

    public String nowDateTimeToStr() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDateTime.now().format(format);
    }

}
