package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.ExceptionDto;
import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.dto.SignupResponseDto;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.exception.UserException;
import com.sparta.javafeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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

        User user = new User(signupRequest);

        userRepository.save(user);

        return new SignupResponseDto(user);
    }
}
