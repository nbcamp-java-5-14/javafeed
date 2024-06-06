package com.sparta.javafeed.jwt;

import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserRole;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);

        if (StringUtils.hasText(accessToken)) {
            if (jwtUtil.validateToken(accessToken)) {
                validToken(accessToken);
            } else {
                invalidToken(request, response);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void validToken(String token) {
        Claims info = jwtUtil.getUserInfoFromToken(token);

        try {
            setAuthentication(info.getSubject());
        } catch (Exception e) {
            log.error("username = {}, message = {}", info.getSubject(), "인증 정보를 찾을 수 없습니다.");
            throw new CustomException(ErrorType.NOT_FOUND_AUTHENTICATION_INFO);
        }
    }

    private void invalidToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);

        if (StringUtils.hasText(refreshToken)) {
            if (jwtUtil.validateToken(refreshToken) && jwtUtil.existRefreshToken(refreshToken)) {
                Claims info = jwtUtil.getUserInfoFromToken(refreshToken);

                UserRole role = UserRole.valueOf(info.get("role").toString());

                String newAccessToken = jwtUtil.createToken(info.getSubject(), role);

                jwtUtil.setHeaderAccessToken(response, newAccessToken);

                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error("username = {}, message = {}", info.getSubject(), "인증 정보를 찾을 수 없습니다.");
                    throw new CustomException(ErrorType.NOT_FOUND_AUTHENTICATION_INFO);
                }
            } else {
                throw new CustomException(ErrorType.INVALID_REFRESH_TOKEN);
            }
        }
    }

    // 인증 처리
    private void setAuthentication(String accountId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(accountId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String accountId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(accountId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
