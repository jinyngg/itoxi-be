package com.itoxi.petnuri.domain.dailychallenge.util;

import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    public DateTemplate<LocalDate> formatter(DateTimePath<LocalDateTime> reqDateTime) {
        if (dbName.contains("mysql")) {
            return getMySqlDate(reqDateTime);
        }
        return getH2Date(reqDateTime);
    }

    private DateTemplate<LocalDate> getH2Date(DateTimePath<LocalDateTime> reqDateTime) {
        return Expressions.dateTemplate(LocalDate.class, "FORMATDATETIME({0}, 'yyyy-MM-dd')", reqDateTime);
    }

    private DateTemplate<LocalDate> getMySqlDate(DateTimePath<LocalDateTime> reqDateTime) {
        return Expressions.dateTemplate(LocalDate.class, "DATE_FORMAT({0}, '%Y-%m-%d')", reqDateTime);
    }

}
