package com.sparta.javafeed.entity;

import com.sparta.javafeed.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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

    @Column
    private String refreshToken;

    @Column
    private LocalDateTime userStatusModifiedAt;

    public User(SignupRequestDto signupRequest) {
        this.accountId = signupRequest.getAccountId();
        this.password = signupRequest.getPassword();
        this.name = signupRequest.getName();
        this.email = signupRequest.getEmail();
    }
}
