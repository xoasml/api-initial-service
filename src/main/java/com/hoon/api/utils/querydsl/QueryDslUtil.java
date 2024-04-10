package com.hoon.api.utils.querydsl;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class QueryDslUtil {

    /**
     * querydsl Util : select 결과 정렬 메소드
     */
    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

    /**
     * querydsl Util : 날짜 형태 포맷팅
     * format Ex : '%Y-%m-%d %h:%m:%s'
     */
    @Deprecated
    public static DateTemplate<String> getFormatDateToString(Object column, String format) {
        return  Expressions.dateTemplate(
                String.class
                , "DATE_FORMAT({0}, {1})"
                , column
                , ConstantImpl.create(format)
        );
    }

    /**
     * querydsl Util : String으로 들어온 날짜 파라미터 LocalDateTime 로 파싱
     * format Ex : String의 값이 2021년 06월 19일 21시 05분 07초 -> yyyy년 MM월 dd일 HH시 mm분 ss초
     */
    public static LocalDateTime parseStringToLocalDateTime(String param, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(param, formatter);
    }

}
