package com.sparta.javafeed.controller;


import com.sparta.javafeed.dto.*;

import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.dto.SignupResponseDto;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.service.UserService;
import jakarta.validation.Valid;

import com.sparta.javafeed.dto.LoginRequestDto;
import com.sparta.javafeed.dto.LoginResponseDto;
import com.sparta.javafeed.dto.ResponseEntityDto;

import com.sparta.javafeed.jwt.JwtUtil;
import com.sparta.javafeed.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> signupUser(@RequestBody @Valid SignupRequestDto signupRequest) {
        SignupResponseDto responseDto = userService.signupUser(signupRequest);

        ResponseEntityDto<SignupResponseDto> responseEntity = new ResponseEntityDto<>(ResponseStatus.SIGN_UP_SUCCESS, responseDto);

        return ResponseEntity.ok(responseEntity);
    }

    @PatchMapping
    public ResponseEntity<ResponseStatusDto> deactiveUser(@RequestBody PasswordReqeustDto passwordRequest, @AuthenticationPrincipal UserDetailsImpl details) {
        userService.deactiveUser(passwordRequest, details.getUser().getAccountId());
        ResponseStatusDto responseStatusDto = new ResponseStatusDto((ResponseStatus.DEACTIVATE_USER_SUCCESS));
        return new ResponseEntity<>(responseStatusDto, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (HttpServletResponse response, @RequestBody LoginRequestDto requestDto){
        LoginResponseDto responseDto = userService.login(requestDto);

        response.addHeader(JwtUtil.AUTH_ACCESS_HEADER, responseDto.getAccessToken());
        response.addHeader(JwtUtil.AUTH_REFRESH_HEADER, responseDto.getRefreshToken());

        ResponseEntityDto<LoginResponseDto> responseEntity = new ResponseEntityDto<>(ResponseStatus.LOGIN_SUCCESS, responseDto);

        return ResponseEntity.ok(responseEntity);
    }

    @GetMapping
    public ResponseEntity<UserInfoResponseDto> getUser(@AuthenticationPrincipal UserDetailsImpl details){
        UserInfoResponseDto userInfoResponseDto = userService.getUser(details.getUsername());
        return new ResponseEntity<>(userInfoResponseDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseStatusDto> updateUser(@RequestBody @Valid UserInfoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl details){
        userService.updateUser(requestDto, details.getUsername());
        return ResponseEntity.ok(new ResponseStatusDto(ResponseStatus.PROFILE_UPDATE_SUCCESS));
    }

    @PatchMapping("/password")
    public ResponseEntity<ResponseStatusDto> updatePassword(@RequestBody @Valid PasswordUpdateDto requestDto, @AuthenticationPrincipal UserDetailsImpl details){
        userService.updatePassword(requestDto, details.getUsername());
        return ResponseEntity.ok(new ResponseStatusDto(ResponseStatus.PASSWORD_UPDATE_SUCCESS));
    }
}
