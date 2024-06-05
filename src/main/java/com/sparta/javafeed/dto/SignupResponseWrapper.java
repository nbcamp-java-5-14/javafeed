package com.sparta.javafeed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseWrapper {
    private SignupResponseDto signupResponseDto;
    private ResponseStatus responseStatus;

    public SignupResponseWrapper(SignupResponseDto responseDto, ResponseStatus responseStatus) {
        this.signupResponseDto = responseDto;
        this.responseStatus = responseStatus;
    }
}
