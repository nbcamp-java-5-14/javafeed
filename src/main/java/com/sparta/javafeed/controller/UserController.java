package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.dto.SignupResponseDto;
import com.sparta.javafeed.dto.SignupResponseWrapper;
import com.sparta.javafeed.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<SignupResponseWrapper> signupUser(@RequestBody @Valid SignupRequestDto signupRequest) {

        return userService.signupUser(signupRequest);
    }
}
