package com.sparta.javafeed.jwt;

import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserRole;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.repository.UserRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JwtUtil")
public class JwtUtil {

    // access 토큰 헤더
    public static final String AUTH_ACCESS_HEADER = "AccessToken";
    // refresh 토큰 헤더
    public static final String AUTH_REFRESH_HEADER = "RefreshToken";
    // 사용자 권한
    public static final String AUTHORIZATION_KEY = "auth";
    // 토큰 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // access 토큰 만료 시간 (30분)
    private final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    // refresh 토큰 만료 시간 (2주)
    private final long REFRESH_TOKEN_EXPIRE_TIME = 14 * 24 * 60 * 60 * 1000L;
    // 로그아웃 refresh 토큰 블랙리스트
    private Set<String> blacklist = ConcurrentHashMap.newKeySet();

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Access token 생성
    public String createToken(String accountId, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(accountId)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // refresh token 생성
    public String createRefreshToken(String accountId, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(accountId)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // 헤더에서 access 토큰 가져오기
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTH_ACCESS_HEADER);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    // 헤더에서 refresh 토큰 가져오기
    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader(AUTH_REFRESH_HEADER);
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith(BEARER_PREFIX)) {
            return refreshToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        if (isblacklistToken(token)) {
            throw new CustomException(ErrorType.LOGGED_OUT_TOKEN);
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new CustomException(ErrorType.INVALID_JWT);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw new CustomException(ErrorType.EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new CustomException(ErrorType.INVALID_JWT);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new CustomException(ErrorType.INVALID_JWT);
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // refresh 토큰이 사용자 정보에 존재하는지 확인
    public boolean existRefreshToken(String refreshToken) {
        Claims info = getUserInfoFromToken(refreshToken);
        User user = userRepository.findByAccountId(info.getSubject()).orElseThrow(
                ()-> new RuntimeException("User not found"));
        return user.checkRefreshToken(refreshToken);
    }

    // 헤더에 access 토큰 담기
    public void setHeaderAccessToken(HttpServletResponse response, String newAccessToken) {
        response.setHeader(AUTH_ACCESS_HEADER, newAccessToken);
    }

    // 블략리스트에 추가
    public void addBlacklistToken(String token) {
        blacklist.add(token);
    }

    // 블랙리스트인지 확인
    private boolean isblacklistToken(String token) {
        return blacklist.contains(token);
    }
}
