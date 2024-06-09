package com.sparta.javafeed.entity;

import com.sparta.javafeed.dto.PasswordReqeustDto;
import com.sparta.javafeed.dto.SignupRequestDto;
import com.sparta.javafeed.dto.UserInfoRequestDto;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserRole;
import com.sparta.javafeed.enums.UserStatus;
import com.sparta.javafeed.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="User")
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Newsfeed> newsfeeds = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String accountId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String intro;

    @Column
    @Enumerated(EnumType.STRING)
    // Eunm 값이 데이터가 숫자로 저장되기 때문에, 스트링으로 찍히도록 하기위함.
    private UserStatus userStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column
    private String refreshToken;

    @Column
    private LocalDateTime userStatusModifiedAt;

    @Column
    private LocalDateTime emailSentAt;

    public User(SignupRequestDto signupRequest, String encodedPassword) {
        this.accountId = signupRequest.getAccountId();
        this.password = encodedPassword;
        this.name = signupRequest.getName();
        this.email = signupRequest.getEmail();
        this.userStatus = UserStatus.BEFORE_VERIFIED;
        this.userRole = UserRole.USER;
    }

    public void updateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
        this.userStatusModifiedAt = LocalDateTime.now();
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean checkRefreshToken(String refreshToken) {
        if (this.refreshToken != null && refreshToken.equals(this.refreshToken)) {
            return true;
        }
        return false;
    }

    public void updateUserInfo(UserInfoRequestDto requestDto) {
        this.name = requestDto.getName();
        this.email = requestDto.getEmail();
        this.intro = requestDto.getIntro();
    }

    public void updatePassword(String encodedNewPassword) {
        this.password = encodedNewPassword;
    }
}
