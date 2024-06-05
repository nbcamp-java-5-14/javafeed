package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.ResponseStatus;
import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.dto.SignupResponseDto;
import com.sparta.javafeed.dto.UserResponseDto;
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

    public ResponseEntity<SignupResponseWrapper> signupUser(SignupRequestDto signupRequest) {
        User user = new User(signupRequest);

        userRepository.save(user);

        SignupResponseDto responseDto = new SignupResponseDto(user);

        ResponseStatus responseStatus = new ResponseStatus(HttpStatus.OK.value(), "회원가입에 성공하였습니다.");

        SignupResponseWrapper responseWrapper = new SignupResponseWrapper(responseDto, responseStatus);

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    public ResponseEntity<UserResponseDto> getUser(String username) {
        // 임시
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        return new ResponseEntity<>(new UserResponseDto(user), HttpStatus.OK);
    }
}
