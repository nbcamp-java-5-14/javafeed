package com.sparta.javafeed.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EmailSendResponseDto {
    private String authCode;
    private LocalDateTime sentAt;
    private LocalDateTime expiredAt;

    public EmailSendResponseDto(String authCode, LocalDateTime sentAt, LocalDateTime expiredAt) {
        this.authCode = authCode;
        this.sentAt = sentAt;
        this.expiredAt = expiredAt;
    }
}
