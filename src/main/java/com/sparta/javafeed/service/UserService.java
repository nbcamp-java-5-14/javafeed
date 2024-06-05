package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.ResponseStatus;
import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.dto.SignupResponseDto;
import com.sparta.javafeed.dto.SignupResponseWrapper;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public SignupResponseDto signupUser(SignupRequestDto signupRequest) {
        User user = new User(signupRequest);

        userRepository.save(user);

        return new SignupResponseDto(user);
    }
}
