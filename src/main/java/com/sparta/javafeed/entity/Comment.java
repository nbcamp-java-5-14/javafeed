package com.sparta.javafeed.entity;

import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserRole;
import com.sparta.javafeed.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "newsfeed_id", nullable = false)
    private Newsfeed newsfeed;

    @Column(nullable = false)
    private String description;

    @Column
    private Long likeCnt;

    public Comment(User user, Newsfeed newsfeed, String description) {
        this.user = user;
        this.newsfeed = newsfeed;
        this.description = description;
        this.likeCnt = 0L;
    }

    public void validate(User user) {
        if (user.getUserRole() == UserRole.USER && !this.user.getId().equals(user.getId())) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
    }

    public void update(String description) {
        this.description = description;
    }

    public void increaseLikeCnt() {
        this.likeCnt++;
    }
    public void decreaseLikeCnt() {
        this.likeCnt--;
    }
}
