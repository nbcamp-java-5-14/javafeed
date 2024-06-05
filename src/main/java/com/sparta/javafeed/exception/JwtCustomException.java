package com.sparta.javafeed.exception;

import com.sparta.javafeed.enums.ErrorType;
import lombok.Getter;

@Getter
public class JwtCustomException extends RuntimeException {

    private String result;
    private ErrorType errorType;

    public JwtCustomException(ErrorType errorType) {
        this.result = "ERROR";
        this.errorType = errorType;
    }

}
