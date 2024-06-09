package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.*;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserStatus;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.jwt.JwtUtil;
import com.sparta.javafeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param signupRequest 회원가입 정보
     * @return 회원 정보
     */
    public SignupResponseDto signupUser(SignupRequestDto signupRequest) {
        // 아이디 중복 검증 로직
        Optional<User> checkAccountId = userRepository.findByAccountId(signupRequest.getAccountId());
        if (checkAccountId.isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_ACCOUNT_ID);
        }

        // 이메일 중복 검증 로직
        Optional<User> checkEmail = userRepository.findByEmail(signupRequest.getEmail());
        if (checkEmail.isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_EMAIL);
        }


        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User(signupRequest, encodedPassword);

        userRepository.save(user);

        return new SignupResponseDto(user);
    }

    /**
     * 회원 상태 비활성화
     * @param passwordRequest 비밀번호
     * @param accountId 회원 ID
     */
    @Transactional
    public void deactiveUser(PasswordReqeustDto passwordRequest, String accountId) {
        User userByAccountId = this.findByAccountId(accountId);
        //기존 탈퇴 회원 검증 로직
        if (userByAccountId.getUserStatus().equals(UserStatus.DEACTIVATE)) {
            throw new CustomException(ErrorType.DEACTIVATE_USER);
        }

        //비밀번호 확인 검증 로직
        if (!passwordEncoder.matches(passwordRequest.getPassword(), userByAccountId.getPassword())) {
            throw new CustomException(ErrorType.INVALID_PASSWORD);
        }

        userByAccountId.updateUserStatus(UserStatus.DEACTIVATE);
    }

    /**
     * 회원 상세정보 조회
     * @param accountId 회원 ID
     * @return 회원 정보
     */
    public UserInfoResponseDto getUser(String accountId) {
        User byAccountId = this.findByAccountId(accountId);

        return new UserInfoResponseDto(byAccountId);
    }

    /**
     * 회원 정보 수정
     * @param requestDto 수정 정보
     * @param accountId 회원 ID
     */
    @Transactional
    public void updateUser(UserInfoRequestDto requestDto, String accountId) {
        User byAccountId = this.findByAccountId(accountId);

        byAccountId.updateUserInfo(requestDto);
    }

    /**
     * 회원 비밀번호 변경
     * @param requestDto 비밀번호
     * @param accountId 회원 ID
     */
    @Transactional
    public void updatePassword(PasswordUpdateDto requestDto, String accountId) {
        User byAccountId = this.findByAccountId(accountId);

        // 기존 패스워드가 맞는지 확인
        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), byAccountId.getPassword())){
            throw new CustomException(ErrorType.INVALID_PASSWORD);
        }

        // 새로운 패스워드가 기존 패스워드와 같은지 확인
        if (passwordEncoder.matches(requestDto.getNewPassword(), byAccountId.getPassword())) {
            throw new CustomException(ErrorType.DUPLICATE_PASSWORD);
        }

        String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());

        byAccountId.updatePassword(encodedNewPassword);
    }

    /**
     * 로그아웃
     * @param user 회원 정보
     * @param accessToken 토큰
     * @param refreshToken 토큰
     */
    @Transactional
    public void loout(User user, String accessToken, String refreshToken) {
        if (user==null) {
            throw new CustomException(ErrorType.LOGGED_OUT_TOKEN);
        }
        User user2 = userRepository.findByAccountId(user.getAccountId()).orElseThrow(
                ()-> new CustomException(ErrorType.NOT_FOUND_USER));

        // 회원 refresh 토큰 초기화
        user2.saveRefreshToken("");

        // 블랙리스트 추가
        jwtUtil.addBlacklistToken(accessToken);
        jwtUtil.addBlacklistToken(refreshToken);
    }


    /**
     * 이메일 전송 후, user.eamilSent 에 전송시간 저장
     * @param email
     */
    public void updateUserEmailSent(String email, LocalDateTime sentAt) {
        //회원 존재여부 재확인 로직
        User user = findByEmail(email);

        //이메일 전송 시간 저장
        user.setEmailSentAt(sentAt);

        //DB에 저장
        userRepository.save(user);
    }


    /**
     * 이메일 인증 후 UserStatus ACTIVE로 업데이트
     * @param requestDto 이메일(email) 및 인증코드(authNum)
     */
    public void updateUserStatus(EmailVerifyCheckRequestDto requestDto) {
        //회원 존재여부 재확인 로직
        User user = findByEmail(requestDto.getEmail());

        //회원 상태 ACTIVE로 변경
        user.updateUserStatus(UserStatus.ACTIVE);

        //DB에 저장
        userRepository.save(user);
    }



    /**
     * 회원 Entity 조회
     * @param accountId 회원 ID
     * @return 회원 Entity
     */
    public User findByAccountId(String accountId){
        return userRepository.findByAccountId(accountId).orElseThrow(
                () -> new CustomException(ErrorType.INVALID_ACCOUNT_ID)
        );
    }

    /**
     * 회원 Entity 조회
     * @param email 회원 EMAIL
     * @return 회원 Entity
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorType.INVALID_EMAIL)
        );
    }

}
