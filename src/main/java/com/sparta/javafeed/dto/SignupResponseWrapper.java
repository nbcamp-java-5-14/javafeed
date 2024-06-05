package com.sparta.javafeed.dto;

import com.sparta.javafeed.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseWrapper {
    private SignupResponseDto signupResponseDto;
    private SignupResponseStatus responseStatus;

    public SignupResponseWrapper(SignupResponseDto responseDto, ResponseStatus responseStatus) {
        this.signupResponseDto = responseDto;
        this.responseStatus = new SignupResponseStatus(responseStatus.getHttpStatus().value(), responseStatus.getMessage());
    }
}
