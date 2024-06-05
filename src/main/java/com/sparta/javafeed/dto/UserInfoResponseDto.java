package com.sparta.javafeed.dto;

import com.sparta.javafeed.entity.User;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private String accountId;
    private String name;
    private String email;
    private String intro;

    public UserInfoResponseDto(User user) {
        this.accountId = user.getAccountId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.intro = user.getIntro();
    }
}
