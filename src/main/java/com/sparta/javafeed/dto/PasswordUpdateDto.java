package com.sparta.javafeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PasswordUpdateDto {
    @NotBlank(message = "현재 비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-={}|:;<>?,./~`])[a-zA-Z0-9!@#$%^&*()_+\\-={}|:;<>?,./~`]{10,}$", message = "비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 하며, 최소 10글자 이상이어야 합니다.")
    private String currentPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-={}|:;<>?,./~`])[a-zA-Z0-9!@#$%^&*()_+\\-={}|:;<>?,./~`]{10,}$", message = "비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 하며, 최소 10글자 이상이어야 합니다.")
    private String newPassword;
}
