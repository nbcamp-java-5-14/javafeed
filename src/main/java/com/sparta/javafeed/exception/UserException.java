package com.sparta.javafeed.exception;

import com.sparta.javafeed.enums.ErrorType;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private String result;
    private ErrorType errorType;

    public UserException(ErrorType errorType) {
        this.result = "ERROR";
        this.errorType = errorType;
    }
}