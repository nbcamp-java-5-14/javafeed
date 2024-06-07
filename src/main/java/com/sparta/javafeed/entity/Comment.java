package com.sparta.javafeed.entity;

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
    }
}
