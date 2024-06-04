package com.sparta.javafeed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @NotBlank(message = "사용자 아이디를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{10,20}$", message = "영문 대소문자와 숫자 조합만 가능하며, 10자 이상 20자 이하여야 합니다.")
    private String accountId;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-={}|:;<>?,./~`])[a-zA-Z0-9!@#$%^&*()_+\\-={}|:;<>?,./~`]{10,}$", message = "비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 하며, 최소 10글자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @Email(message = "올바른 이메일 형식을 입력해 주세요.")
    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;
}
