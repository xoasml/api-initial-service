package com.hoon.api.utils.advice;

import com.hoon.api.utils.exception.MailSendException;
import com.hoon.api.utils.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Slf4j
@Order(1)
@RestControllerAdvice
public class ServerAdvice {

    /*
        서버 관련 에러 핸들링 클래스

        500 - 연동 중인 서비스에 연결할 수 없음
        500 - 메일 전송 실패 (사용중이지 않음)
     */

    /**
     * 500 연동 중인 서비스에 연결할 수 없음
     */
    @ExceptionHandler(value = ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> resourceAccessException(ResourceAccessException e, HttpServletRequest httpServletRequest) {


        log.error("[ERROR] [OTHER SERVICE CONNECTION ERROR] {}", e.getClass().getName());
        log.error(e.getMessage());
        log.error("[ERROR]");

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .error("연동 중인 서비스에 연결할 수 없습니다.")
                        .path(httpServletRequest.getRequestURI())
                        .info(List.of(e.getMessage()))
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * 500 메일 전송 실패 (사용중이지 않음)
     */
    @ExceptionHandler(value = MailSendException.class)
    public ResponseEntity<ErrorResponse> mailSendException(MailSendException e, HttpServletRequest httpServletRequest) {

        log.error("[ERROR] [MAIL SEND FAIL] {}", e.getClass().getName());
        log.info(e.getMessage());
        log.error("[ERROR]");

         return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .error("메일 발송 에러")
                        .path(httpServletRequest.getRequestURI())
                        .info(List.of(e.getMessage()))
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
