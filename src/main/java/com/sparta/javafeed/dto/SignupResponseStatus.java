package com.sparta.javafeed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseStatus {
    private int status;
    private String message;

    public SignupResponseStatus(int value, String s) {
        this.status = value;
        this.message = s;
    }
}