package com.sparta.javafeed.dto;

import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ResponseStatusDto {
    private int status;
    private String message;

    public ResponseStatusDto(ResponseStatus status) {
        this.status = status.getHttpStatus().value();
        this.message = status.getMessage();
    }
}

