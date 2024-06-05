package com.sparta.javafeed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String accountId;
    private String accessToken;
    private String refreshToken;
}
