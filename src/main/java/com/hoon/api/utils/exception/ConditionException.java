package com.hoon.api.utils.exception;

import com.hoon.api.utils.advice.dto.Error;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ConditionException extends RuntimeException{

    Error error;

    public ConditionException(Error error) {
        this.error = error;
    }

}
