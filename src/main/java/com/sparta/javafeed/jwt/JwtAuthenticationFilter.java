package com.sparta.javafeed.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.javafeed.dto.ExceptionDto;
import com.sparta.javafeed.dto.LoginRequestDto;
import com.sparta.javafeed.dto.ResponseStatusDto;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.enums.UserRole;
import com.sparta.javafeed.enums.UserStatus;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.repository.UserRepository;
import com.sparta.javafeed.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        // Security 로그인 요청을 처리할 URL 패턴
        setFilterProcessesUrl("/users/login");
    }

    // 로그인 요청 처리
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getAccountId(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 성공 처리
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        User user2 = ((UserDetailsImpl) auth.getPrincipal()).getUser();
        if (UserStatus.DEACTIVATE.equals(user2.getUserStatus())) {
            throw new CustomException(ErrorType.DEACTIVATE_USER);
        }

        String username = ((UserDetailsImpl) auth.getPrincipal()).getUsername();
        UserRole role = ((UserDetailsImpl) auth.getPrincipal()).getUser().getUserRole();

        String accessToken = jwtUtil.createToken(username, role);
        String refreshToken = jwtUtil.createRefreshToken(username, role);

        User user = ((UserDetailsImpl) auth.getPrincipal()).getUser();
        user.saveRefreshToken(refreshToken.substring(7));
        userRepository.save(user);

        response.addHeader(JwtUtil.AUTH_ACCESS_HEADER, accessToken);
        response.addHeader(JwtUtil.AUTH_REFRESH_HEADER, refreshToken);

        // 로그인 성공 메시지
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseStatusDto(ResponseStatus.LOGIN_SUCCESS)));
        response.getWriter().flush();
    }

    // 로그인 실패 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ErrorType errorType = ErrorType.NOT_FOUND_AUTHENTICATION_INFO;
        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ExceptionDto(errorType)));
        response.getWriter().flush();
    }
}
