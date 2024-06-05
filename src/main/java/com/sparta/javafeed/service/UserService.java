package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.*;
import com.sparta.javafeed.dto.ExceptionDto;
import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.dto.SignupResponseDto;

import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserStatus;
import com.sparta.javafeed.exception.UserException;
import com.sparta.javafeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import com.sparta.javafeed.dto.LoginRequestDto;
import com.sparta.javafeed.dto.LoginResponseDto;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserRole;
import com.sparta.javafeed.exception.UserException;
import com.sparta.javafeed.jwt.JwtUtil;
import com.sparta.javafeed.repository.UserRepository;
import com.sparta.javafeed.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signupUser(SignupRequestDto signupRequest) {
        // 아이디 중복 검증 로직
        Optional<User> checkAccountId = userRepository.findByAccountId(signupRequest.getAccountId());
        if (checkAccountId.isPresent()) {
            throw new UserException(ErrorType.DUPLICATE_ACCOUNT_ID);
        }

        // 이메일 중복 검증 로직
        Optional<User> checkEmail = userRepository.findByEmail(signupRequest.getEmail());
        if (checkEmail.isPresent()) {
            throw new UserException(ErrorType.DUPLICATE_EMAIL);
        }


        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User(signupRequest, encodedPassword);

        userRepository.save(user);

        return new SignupResponseDto(user);
    }

    @Transactional
    public void deactiveUser(PasswordReqeustDto passwordRequest, String accountId) {
        User userByAccountId = this.findByAccountId(accountId);
        //기존 탈퇴 회원 검증 로직
        if (userByAccountId.getUserStatus().equals(UserStatus.DEACTIVATE)) {
            throw new UserException(ErrorType.DEACTIVATE_USER);
        }

        //비밀번호 확인 검증 로직
        if (!passwordEncoder.matches(passwordRequest.getPassword(), userByAccountId.getPassword())) {
            throw new UserException(ErrorType.INVALID_PASSWORD);
        }

        userByAccountId.deactiveUser(UserStatus.DEACTIVATE);
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByAccountId(requestDto.getAccountId()).orElseThrow(
                ()-> new UserException(ErrorType.NOT_FOUND_USER));

        String accessToken = jwtUtil.createToken(user.getAccountId(), user.getUserRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getAccountId(), user.getUserRole());

        user.saveRefreshToken(refreshToken.substring(7));

        return new LoginResponseDto(user.getAccountId(), accessToken, refreshToken);
    }

    public UserInfoResponseDto getUser(String accountId) {
        User byAccountId = this.findByAccountId(accountId);

        return new UserInfoResponseDto(byAccountId);
    }

    @Transactional
    public String updateUser(UserInfoRequestDto requestDto, String accountId) {
        User byAccountId = this.findByAccountId(accountId);

        byAccountId.updateUserInfo(requestDto);

        return "프로필이 수정되었습니다.";
    }

    @Transactional
    public String updatePassword(PasswordUpdateDto requestDto, String accountId) {
        User byAccountId = this.findByAccountId(accountId);

        // 기존 패스워드가 맞는지 확인
        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), byAccountId.getPassword())){
            throw new UserException(ErrorType.INVALID_PASSWORD);
        }

        // 새로운 패스워드가 기존 패스워드와 같은지 확인
        if (passwordEncoder.matches(requestDto.getNewPassword(), byAccountId.getPassword())) {
            throw new UserException(ErrorType.DUPLICATE_PASSWORD);
        }

        String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());

        byAccountId.updatePassword(encodedNewPassword);

        return "비밀번호가 수정되었습니다.";
    }


    private User findByAccountId(String accountId){
        return userRepository.findByAccountId(accountId).orElseThrow(
                () -> new UserException(ErrorType.INVALID_ACCOUNT_ID)
        );
    }


}
