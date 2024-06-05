package com.sparta.javafeed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStatus {
    private int status;
    private String message;

    public ResponseStatus(int value, String s) {
        this.status = value;
        this.message = s;
    }
}