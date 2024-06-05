package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.LoginRequestDto;
import com.sparta.javafeed.dto.LoginResponseDto;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserRole;
import com.sparta.javafeed.exception.UserException;
import com.sparta.javafeed.jwt.JwtUtil;
import com.sparta.javafeed.repository.UserRepository;
import com.sparta.javafeed.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getAccountId(),
                            requestDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Long id = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getId();
            String accountId = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
            UserRole role = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserRole();

            String accessToken = jwtUtil.createToken(accountId, role);
            String refreshToken = jwtUtil.createRefreshToken(accountId, role);

            User user = userRepository.findById(id).orElseThrow(
                    ()-> new UserException(ErrorType.NOT_FOUND_USER));

            user.saveRefreshToken(refreshToken.substring(7));

            return new LoginResponseDto(accountId, accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new UserException(ErrorType.NOT_FOUND_USER);
        }
    }
}
