package com.hoon.api.utils.exception;

public class TokenNotVailidException extends RuntimeException{

    public TokenNotVailidException() {
        super("토큰이 유효하지 않거나 만료되었습니다. 다시 로그인 하십시오.");
    }
}
