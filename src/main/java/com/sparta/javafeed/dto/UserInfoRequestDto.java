package com.sparta.javafeed.dto;

import com.sparta.javafeed.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserInfoRequestDto {
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    private String intro; // 한 줄 소개는 비울 수 있음
}
