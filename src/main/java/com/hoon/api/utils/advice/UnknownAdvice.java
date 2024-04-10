package com.hoon.api.utils.advice;

import com.hoon.api.utils.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@Order(99)
@RestControllerAdvice
public class UnknownAdvice {

    /*
        핸들러로 정의하지 않은 익셉션을 핸들링 하는 클래스
        500 - 정의하지 않은 서버 에러
     */

    /**
     * 500 정의하지 않은 서버 에러
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e, HttpServletRequest httpServletRequest) {

        log.error("[ERROR] [SERVER ERROR] {}", e.getClass().getName());
        e.printStackTrace();
        log.error("[ERROR]");

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .path(httpServletRequest.getRequestURI())
                        .error("unknown error")
                        .info(List.of("반복 발생 시 관리자에게 문의 바랍니다."))
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
