package com.sparta.javafeed.dto;

import com.sparta.javafeed.enums.ResponseStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseEntityDto<T> {

    private String status;
    private String message;
    private T data;

    public ResponseEntityDto(ResponseStatus responseStatus, T data) {
        this.status = responseStatus.getHttpStatus().toString();
        this.message = responseStatus.getMessage();
        this.data = data;
    }
}
