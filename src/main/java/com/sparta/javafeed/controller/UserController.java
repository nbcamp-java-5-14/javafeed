package com.sparta.javafeed.controller;


import com.sparta.javafeed.dto.*;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.jwt.JwtUtil;
import com.sparta.javafeed.security.UserDetailsImpl;
import com.sparta.javafeed.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     * @param signupRequest 회원가입 정보
     * @return 회원 정보, 응답 상태
     */
    @PostMapping
    public ResponseEntity<?> signupUser(@RequestBody @Valid SignupRequestDto signupRequest) {
        SignupResponseDto responseDto = userService.signupUser(signupRequest);

        ResponseEntityDto<SignupResponseDto> responseEntity = new ResponseEntityDto<>(ResponseStatus.SIGN_UP_SUCCESS, responseDto);

        return ResponseEntity.ok(responseEntity);
    }

    /**
     * 회원 상태 비활성화
     * @param passwordRequest 비밀번호
     * @param details 회원 정보
     * @return 응답 상태
     */
    @PatchMapping
    public ResponseEntity<ResponseStatusDto> deactiveUser(@RequestBody PasswordReqeustDto passwordRequest, @AuthenticationPrincipal UserDetailsImpl details) {
        userService.deactiveUser(passwordRequest, details.getUser().getAccountId());
        ResponseStatusDto responseStatusDto = new ResponseStatusDto((ResponseStatus.DEACTIVATE_USER_SUCCESS));
        return new ResponseEntity<>(responseStatusDto, HttpStatus.ACCEPTED);
    }

    /**
     * 로그아웃
     * @param details 회원 정보
     * @param request 요청 객체
     * @return 응답 상태
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl details, HttpServletRequest request) {
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);

        userService.loout(details.getUser(), accessToken, refreshToken);

        return ResponseEntity.ok(new ResponseStatusDto(ResponseStatus.LOGOUT_SUCCESS));
    }

    /**
     * 회원 상세정보 조회
     * @param details 회원 정보
     * @return 회원 정보
     */
    @GetMapping
    public ResponseEntity<UserInfoResponseDto> getUser(@AuthenticationPrincipal UserDetailsImpl details){
        UserInfoResponseDto userInfoResponseDto = userService.getUser(details.getUsername());
        return new ResponseEntity<>(userInfoResponseDto, HttpStatus.OK);
    }

    /**
     * 회원 정보 수정
     * @param requestDto 수정 정보
     * @param details 회원 정보
     * @return 응답 상태
     */
    @PutMapping
    public ResponseEntity<ResponseStatusDto> updateUser(@RequestBody @Valid UserInfoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl details){
        userService.updateUser(requestDto, details.getUsername());
        return ResponseEntity.ok(new ResponseStatusDto(ResponseStatus.PROFILE_UPDATE_SUCCESS));
    }

    /**
     * 회원 비밀번호 변경
     * @param requestDto 비밀번호
     * @param details 회원 정보
     * @return 응답 상태
     */
    @PatchMapping("/password")
    public ResponseEntity<ResponseStatusDto> updatePassword(@RequestBody @Valid PasswordUpdateDto requestDto, @AuthenticationPrincipal UserDetailsImpl details){
        userService.updatePassword(requestDto, details.getUsername());
        return ResponseEntity.ok(new ResponseStatusDto(ResponseStatus.PASSWORD_UPDATE_SUCCESS));
    }
}
