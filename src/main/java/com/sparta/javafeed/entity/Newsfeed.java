package com.sparta.javafeed.entity;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name="Newsfeed")
public class Newsfeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;

    public Newsfeed(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.user.getNewsfeeds().add(this);
    }

    public void update(NewsfeedRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
    }
}
