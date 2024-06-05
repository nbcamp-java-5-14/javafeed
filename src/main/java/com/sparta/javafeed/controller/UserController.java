package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.dto.UserInfoRequestDto;
import com.sparta.javafeed.dto.UserInfoResponseDto;
import com.sparta.javafeed.dto.SignupResponseWrapper;
import com.sparta.javafeed.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<SignupResponseWrapper> signupUser(@RequestBody @Valid SignupRequestDto signupRequest) {
        return userService.signupUser(signupRequest);
    }

    @GetMapping
    public ResponseEntity<UserInfoResponseDto> getUser(@AuthenticationPrincipal UserDetails details){
        return userService.getUser(details.getUsername());
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserInfoRequestDto requestDto, @AuthenticationPrincipal UserDetails details){
        return userService.updateUser(requestDto, details.getUsername());
    }
}
