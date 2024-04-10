package com.hoon.api.utils.advice;

import com.hoon.api.utils.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@Order(0)
@RestControllerAdvice
public class AuthAdvice {

    /*
        okta 토큰의 권한(permission) 인증 관련 핸들링 클래스
        403 - 권한 체크
     */

    /**
     * 권한이 없는 경우 해당 핸들러를 타 403 에러를 발생시킨다.
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(AccessDeniedException e, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                       .status(HttpStatus.FORBIDDEN.toString())
                       .error("접근 권한이 없습니다.")
                       .path(httpServletRequest.getRequestURI())
                       .info(List.of(e.getMessage()))
                       .build()
               , HttpStatus.FORBIDDEN);
    }

}
