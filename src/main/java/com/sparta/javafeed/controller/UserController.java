package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.*;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.jwt.JwtUtil;
import com.sparta.javafeed.security.UserDetailsImpl;
import com.sparta.javafeed.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<SignupResponseWrapper> signupUser(@RequestBody @Valid SignupRequestDto signupRequest) {
        SignupResponseDto responseDto = userService.signupUser(signupRequest);

        ResponseStatus responseStatus = ResponseStatus.SIGN_UP_SUCCESS;

        SignupResponseWrapper responseWrapper = new SignupResponseWrapper(responseDto, responseStatus);

        return new ResponseEntity<>(responseWrapper, responseStatus.getHttpStatus());

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
    public ResponseEntity<String> updateUser(@RequestBody @Valid UserInfoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl details){
        String message = userService.updateUser(requestDto, details.getUsername());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid PasswordRequestDto requestDto){
        String message = userService.updatePassword(requestDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
