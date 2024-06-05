package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.ExceptionDto;
import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.dto.SignupResponseDto;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.exception.UserException;
import com.sparta.javafeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signupUser(SignupRequestDto signupRequest) {
        // 아이디 중복 검증 로직
        Optional<User> checkAccountId = userRepository.findByAccountId(signupRequest.getAccountId());
        if (checkAccountId.isPresent()) {
            throw new UserException(ErrorType.DUPLICATE_ACCOUNT_ID);
        }

        // 이메일 중복 검증 로직
        Optional<User> checkEmail = userRepository.findByEmail(signupRequest.getEmail());
        if (checkEmail.isPresent()) {
            throw new UserException(ErrorType.DUPLICATE_EMAIL);
        }


        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User(signupRequest, encodedPassword);

        userRepository.save(user);

        return new SignupResponseDto(user);
    }

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
