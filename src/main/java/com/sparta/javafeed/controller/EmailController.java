package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.EmailCheckRequestDto;
import com.sparta.javafeed.dto.EmailVerifyCheckRequestDto;
import com.sparta.javafeed.security.UserDetailsImpl;
import com.sparta.javafeed.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    /**
     * 이메일 발송
     * @param details 회원 정보
     * @param request 요청 객체
     * @return 인증번호
     */
    @PostMapping("/email")
    public String EmailCheck(@RequestBody @Valid EmailCheckRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl details) throws MessagingException, UnsupportedEncodingException {
        String authCode = emailService.sendEmail(requestDto.getEmail(), details.getUser().getEmail());
        return authCode;
    }


    /**
     * 이메일 인증 (인증번호 확인 후 userStatsus : ACTIVE 처리)
     * @param details 회원 정보
     * @param request 요청 객체
     * @return 인증 성공 여부
     */
    @PostMapping("/email/verify")
    public String verifyEmail(@RequestBody @Valid  EmailVerifyCheckRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl details) {
        Boolean isVerified = emailService.verifyCode(requestDto, details.getUser());

        if (isVerified) {
            return "인증 성공";
        } else {
            return "인증 코드 불일치";
        }
    }
}