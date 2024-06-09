package com.sparta.javafeed.entity;

import com.sparta.javafeed.dto.S3ResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Profile extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String saveFileName;

    @Column(nullable = false)
    private String url;

    public Profile(S3ResponseDto responseDto, User userByAccountId) {
        this.user = userByAccountId;
        this.saveFileName = responseDto.getSaveFileName();
        this.url = responseDto.getUrl();
    }

    public void update(S3ResponseDto responseDto) {
        this.saveFileName = responseDto.getSaveFileName();
        this.url = responseDto.getUrl();
    }
}
