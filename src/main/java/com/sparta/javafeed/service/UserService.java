package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.*;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.exception.UserException;
import com.sparta.javafeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<SignupResponseWrapper> signupUser(SignupRequestDto signupRequest) {
        User user = new User(signupRequest);

        userRepository.save(user);

        SignupResponseDto responseDto = new SignupResponseDto(user);

        ResponseStatus responseStatus = new ResponseStatus(HttpStatus.OK.value(), "회원가입에 성공하였습니다.");

        SignupResponseWrapper responseWrapper = new SignupResponseWrapper(responseDto, responseStatus);

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    public ResponseEntity<UserInfoResponseDto> getUser(String username) {
        User byAccountId = this.findByAccountId(username);

        return new ResponseEntity<>(new UserInfoResponseDto(byAccountId), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateUser(UserInfoRequestDto requestDto, String username) {
        User byAccountId = this.findByAccountId(username);

        byAccountId.updateUserInfo(requestDto);

        return new ResponseEntity<>(new UserInfoResponseDto(byAccountId), HttpStatus.OK);
    }

    private User findByAccountId(String accountId){
        return userRepository.findByAccountId(accountId).orElseThrow(
                () -> new UserException(ErrorType.INVALID_ACCOUNT_ID)
        );
    }

}
