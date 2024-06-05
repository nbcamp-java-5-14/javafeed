package com.sparta.javafeed.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    // test controller
    DUPLICATE_ACCOUNT_ID(HttpStatus.LOCKED, "이미 아이디가 존재합니다.."),
    INVALID_ACCOUNT_ID(HttpStatus.UNAUTHORIZED, "아이디가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    DEACTIVATE_USER(HttpStatus.FORBIDDEN, "이미 탈퇴한 회원입니다."),
    DUPLICATE_PASSWORD(HttpStatus.LOCKED, "중복된 비밀번호입니다."),
    NO_AUTHENTICATION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),

    // DUPLICATED_USER(HttpStatus.LOCKED, "중복된 사용자가 존재합니다."),
    // DUPLICATED_EMAIL(HttpStatus.LOCKED, "중복된 Email 입니다."),

    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),
    NOT_FOUND_AUTHENTICATION_INFO(HttpStatus.NOT_FOUND, "인증 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
