package com.sparta.javafeed.entity;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserRole;
import com.sparta.javafeed.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name="Newsfeed")
public class Newsfeed extends Timestamped {

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

    @OneToMany(mappedBy = "newsfeed", orphanRemoval = true)
    private List<Comment> commentList;

    public Newsfeed(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
//        this.user.getNewsfeeds().add(this);
    }

    public void update(NewsfeedRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
    }

    public void userValidate(User user) {
        if(user.getUserRole() == UserRole.USER && !this.user.getName().equals(user.getName())) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
    }
}
