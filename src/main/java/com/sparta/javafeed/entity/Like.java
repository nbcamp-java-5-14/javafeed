package com.sparta.javafeed.entity;

import com.sparta.javafeed.enums.ContentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_like") // like는 예약어라서 오류가 발생함
public class Like extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    public Like(User userByAccountId, Long id, ContentType contentType) {
        this.user = userByAccountId;
        this.contentId = id;
        this.contentType = contentType;
    }
}
