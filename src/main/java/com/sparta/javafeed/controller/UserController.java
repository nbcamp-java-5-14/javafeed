package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.LoginRequestDto;
import com.sparta.javafeed.dto.LoginResponseDto;
import com.sparta.javafeed.dto.ResponseEntityDto;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.jwt.JwtUtil;
import com.sparta.javafeed.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response, @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = userService.login(requestDto);

        response.addHeader(JwtUtil.AUTH_ACCESS_HEADER, responseDto.getAccessToken());
        response.addHeader(JwtUtil.AUTH_REFRESH_HEADER, responseDto.getRefreshToken());

        ResponseEntityDto<LoginResponseDto> responseEntity = new ResponseEntityDto<>(ResponseStatus.LOGIN_SUCCESS, responseDto);

        return ResponseEntity.ok(responseEntity);
    }
}
