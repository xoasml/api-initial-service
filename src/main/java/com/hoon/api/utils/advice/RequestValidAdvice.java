package com.hoon.api.utils.advice;


import com.hoon.api.utils.advice.dto.DBError;
import com.hoon.api.utils.advice.dto.Error;
import com.hoon.api.utils.exception.ConditionException;
import com.hoon.api.utils.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Order(2)
@RestControllerAdvice
public class RequestValidAdvice {

    /*
        Request 데이터 또는 속성에 대한 에러 관련 핸들링 클래스

        400 - 파미터타입 미스매치
        400 - 요청 값 Dto Valid 체크 (Http 요청 시 사용)
        400 - 요청 값 Dto Valid 체크 (@Validated 계층간의 요청에서 사용)
        400 - JSON 포맷, 키 타입 에러
        415 - Content-type 미스매치
        422 - 커스텀 에러
        400 - DB 무결성 에러
        413, 500 - 파일 업로드 에러
     */

    /**
     * 400 파미터타입 미스매치
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest httpServletRequest) {

        log.error("[ERROR] [API VALID MISS] {}", e.getClass().getName());
        log.error(e.getMessage());
        log.error("[ERROR]");


        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .path(httpServletRequest.getRequestURI())
                        .error("파라미터 타입이 잘못되었습니다.")
                        .info(List.of(
                                Error.builder()
                                        .message("파라미터 타입이 잘못되었습니다.")
                                        .key(e.getName())
                                        .value(e.getValue())
                                        .build()
                        ))
                        .build()
                , HttpStatus.BAD_REQUEST);
    }


    /**
     * 400 요청 값 Dto Valid 체크 - Http 요청 시 사용됨
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest httpServletRequest) {

        List<Error> errorList = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            FieldError field = (FieldError) error;

            errorList.add(
                    Error.builder()
                            .message(field.getDefaultMessage())
                            .key(field.getField())
                            .value(field.getRejectedValue() == null ? "null" : field.getRejectedValue().toString())
                            .build());
        });

        log.error("[ERROR] [API VALID MISS] {}", e.getClass().getName());
        errorList.forEach(index -> log.error(String.valueOf(index)));
        log.error("[ERROR]");

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .path(httpServletRequest.getRequestURI())
                        .error("요청 값이 잘못 되었습니다.")
                        .info(errorList)
                        .build()
                , HttpStatus.BAD_REQUEST);

    }

    /**
     * 400 요청 값 Dto Valid 체크 (@Validated) - 계층간의 요청에서 사용됨
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest httpServletRequest) {

        List<Error> errorList = e.getConstraintViolations().stream()
                .map(violation -> Error.builder()
                        .message(violation.getMessage())
                        .key(violation.getPropertyPath().toString())
                        .value(violation.getInvalidValue() == null ? "null" : violation.getInvalidValue().toString())
                        .build())
                .collect(Collectors.toList());

        log.error("[ERROR] [API VALID MISS] {}", e.getClass().getName());
        errorList.forEach(index -> log.error(String.valueOf(index)));
        log.error("[ERROR]");

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .path(httpServletRequest.getRequestURI())
                        .error("요청 값이 잘못 되었습니다.")
                        .info(errorList)
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    /**
     * 405 - 요청 방식 오류 : Ex) GetMapping에 Post로 요청시 발생
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest httpServletRequest) {

        log.error("[ERROR] [MAPPING METHOD MISS] {}", e.getClass().getName());
        log.error(e.getMessage());
        log.error("[ERROR]");

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.METHOD_NOT_ALLOWED.toString())
                        .path(httpServletRequest.getRequestURI())
                        .error("요청 method 타입(GET, POST, DELETE, PUT, ... ) 을 확인해주세요.")
                        .info(List.of(e.getMessage()))
                        .build()
                , HttpStatus.METHOD_NOT_ALLOWED);

    }

    /**
     * 400 JSON 포맷, 키 타입 에러
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest httpServletRequest) {

        log.error("[ERROR] [JSON PARSE MISS] {}", e.getClass().getName());

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .path(httpServletRequest.getRequestURI())
                        .error("JSON 포맷 또는 키의 타입을 확인 하세요.")
                        .info(List.of(e.getMessage()))
                        .build()
                , HttpStatus.BAD_REQUEST);

    }

    /**
     * 415 Content-type 미스매치
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest httpServletRequest) {

        log.error("[ERROR] [UNSUPPORTED MEDIA TYPE] {}", e.getClass().getName());
        log.error("잘못된 Content-type 으로 요청 하셨습니다.");
        log.error("[ERROR]");

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString())
                        .path(httpServletRequest.getRequestURI())
                        .error("잘못된 Content-type 으로 요청 하셨습니다.")
                        .info(List.of(e.getMessage()))
                        .build()
                , HttpStatus.UNSUPPORTED_MEDIA_TYPE);

    }

    /**
     * 422 커스텀 에러
     */
    @ExceptionHandler(value = ConditionException.class)
    public ResponseEntity<ErrorResponse> conditionException(ConditionException e, HttpServletRequest httpServletRequest) {

        log.error("[ERROR] [CONDITION CHECK] {}", e.getClass().getName());
        log.info(e.getError().toString());
        log.error("[ERROR]");

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                        .path(httpServletRequest.getRequestURI())
                        .error("조건에 맞지 않습니다.")
                        .info(List.of(e.getError()))
                        .build()
                , HttpStatus.UNPROCESSABLE_ENTITY);

    }

    /**
     * 400 DB 무결성 에러
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest httpServletRequest) {

        SQLException sqlException = (SQLException) e.getRootCause();
        DBError errorMassage = AdviceUtils.dbErrorSetMassage(sqlException); // DB 에러 메세지 세팅

        log.error("[ERROR] [DATABASE ERROR] {}", e.getClass().getName());
        log.error(errorMassage.toString());
        log.error("[ERROR]");

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.toString())
                        .error("요청값이 DATABASE 속성과 맞지 않습니다.")
                        .path(httpServletRequest.getRequestURI())
                        .info(List.of(errorMassage))
                        .build()
                , HttpStatus.BAD_REQUEST);
    }

    /**
     * 413, 500 파일 업로드 에러
     */
    @ExceptionHandler(value = MultipartException.class)
    public ResponseEntity<ErrorResponse> multipartException(MultipartException e, HttpServletRequest httpServletRequest) {

        log.error("[ERROR] [FILE UPLOAD FAIL] {}", e.getClass().getName());
        log.info(e.getMessage());
        log.error("[ERROR]");

        if (e.getCause().getCause() instanceof SizeLimitExceededException exception) {
            /* 파일 사이즈 초과 에러 */
            return new ResponseEntity<>(
                    ErrorResponse.builder()
                            .status(HttpStatus.PAYLOAD_TOO_LARGE.toString())
                            .error("FILE UPLOAD FAIL")
                            .path(httpServletRequest.getRequestURI())
                            .info(List.of(
                                    Error.builder()
                                            .message("용량은 " + exception.getPermittedSize() / 1024 / 1024 + "MB를 넘을 수 없습니다.")
                                            .key("file size")
                                            .value(exception.getActualSize() / 1024 / 1024 + "MB")
                                            .build()
                            ))
                            .build()
                    , HttpStatus.PAYLOAD_TOO_LARGE);

        } else {
            /* 핸들링 된 파일 업로드 익셉션 외의 에러 */
            return new ResponseEntity<>(
                    ErrorResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .path(httpServletRequest.getRequestURI())
                            .error("FILE UPLOAD FAIL")
                            .info(List.of("반복 발생 시 관리자에게 문의 바랍니다."))
                            .build()
                    , HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
