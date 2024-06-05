package com.sparta.javafeed.dto;

import com.sparta.javafeed.enums.ErrorType;
import lombok.Getter;

@Getter
public class ExceptionDto {
    // 에러타입을 가지고 있는 데이터를 담고 있는 역할

    private String result;
    private ErrorType errorType;
    private String message;

    public ExceptionDto(ErrorType errorType) {
        this.result = "ERROR";
        this.errorType = errorType;
        this.message = errorType.getMessage();
    }

    public ExceptionDto(String message) {
        this.result = "ERROR";
        this.message = message;
    }
}