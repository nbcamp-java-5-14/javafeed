package com.sparta.javafeed.entity;

import com.sparta.javafeed.enums.ContentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Like")
public class Like extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false,name = "content_id")
    private Long contentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "content_type")
    private ContentType contentType;
}
