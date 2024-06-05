package com.sparta.javafeed.dto;

import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.UserStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDto {
    private Long id;
    private String accountId;
    private String password;
    private String name;
    private String email;
    private String intro;
    private UserStatus userStatus;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime userStatusModifiedAt;

    public SignupResponseDto(User user) {
        this.id = user.getId();
        this.accountId = user.getAccountId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.intro = user.getIntro();
        this.email = user.getEmail();
        this.userStatus = user.getUserStatus();
        this.refreshToken = user.getRefreshToken();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
        this.userStatusModifiedAt = user.getUserStatusModifiedAt();
    }
}
