package com.hoon.api.utils.exception;

public class ForbiddenRequestException extends RuntimeException{
    public ForbiddenRequestException() {
        super("권한이 없습니다");
    }
}
