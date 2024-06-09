package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.EmailSendResponseDto;
import com.sparta.javafeed.dto.EmailVerifyCheckRequestDto;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserStatus;
import com.sparta.javafeed.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final UserService userService;
    private String authNum;

    // 인증번호 랜덤 코드로 8자 생성코드
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<8; i++) {
            int idx = random.nextInt(3);

            switch (idx) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        createCode();
        String setFrom = "test@gmail.com";
        String toEmail = email;
        String title = "인증번호";

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject(title);

        // 메일 내용
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;text-align:center;width: 495px;margin: 0 auto;'>";
        msgOfEmail += "<img src='https://ifh.cc/g/VLQ01c.png' style='border-radius: 60px;'>";
        msgOfEmail += "<h1> 안녕하세요 JAVAFEED 입니다. </h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black;font-family:verdana;border-radius: 30px;'>";
        msgOfEmail += "<div style='font-size:130%; margin-top: 20px;'>";
        msgOfEmail += "CODE : <strong>";
        msgOfEmail += authNum + "</strong><div><br/> ";
        msgOfEmail += "</div>";

        message.setFrom(setFrom);
        message.setText(msgOfEmail, "utf-8", "html");

        return message;
    }

    //메일 발송
    public EmailSendResponseDto sendEmail(String email, User loginUser) throws MessagingException, UnsupportedEncodingException {
        //같은 이메일을 입력했는지 검증
        if (!loginUser.getEmail().equals(email)) {
            throw new CustomException(ErrorType.INVALID_EMAIL);
        }

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(email);

        //실제 메일 전송
        emailSender.send(emailForm);

        //전송 시간 기록
        LocalDateTime sentAt = LocalDateTime.now();

        // 회원 테이블에 이메일 전송 시간 저장
        userService.updateUserEmailSent(email, sentAt);

        // 만료 시간 설정 (전송 시간으로부터 3분 후)
        LocalDateTime expiredAt = sentAt.plusMinutes(3);

        return new EmailSendResponseDto(authNum, sentAt, expiredAt);
    }

    //메일 인증번호 확인
    public boolean verifyCode(EmailVerifyCheckRequestDto requestDto, User loginUser) {
        //탈퇴한 회원인지 검증 로직
        if (loginUser.getUserStatus().equals(UserStatus.DEACTIVATE)) {
            throw new CustomException(ErrorType.DEACTIVATE_USER);
        }

        //이미 이메일 확인을 한 회원인지 검증 로직
        if (loginUser.getUserStatus().equals(UserStatus.ACTIVE)) {
            throw new CustomException(ErrorType.VERIFIED_EMAIL);
        }

        //같은 이메일을 입력했는지 검증 로직
        if (!loginUser.getEmail().equals(requestDto.getEmail())) {
            throw new CustomException(ErrorType.INVALID_EMAIL);
        }

        //올바른 인증번호를 입력했는지 확인하는 로직
        if (!authNum.equals(requestDto.getAuthNum())) {
            throw new CustomException(ErrorType.WRONG_AUTH_NUM);
        }

        // 인증 시간 만료 검증 로직
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime emailSentAt = loginUser.getEmailSentAt();
        if (ChronoUnit.SECONDS.between(emailSentAt, now) > 180) {
            throw new CustomException(ErrorType.EXPIRED_AUTH_NUM);
        }

        // 유저 테이블에서 userStatus 업데이트 처리
        userService.updateUserStatus(requestDto);

        return authNum.equals(requestDto.getAuthNum());
    }
}