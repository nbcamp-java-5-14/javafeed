package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void register(SignupRequestDto signupRequest) {
    }
}
