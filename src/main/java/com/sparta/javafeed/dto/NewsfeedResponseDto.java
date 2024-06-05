package com.sparta.javafeed.dto;

import com.sparta.javafeed.entity.Newsfeed;
import lombok.*;

import java.time.LocalDateTime;

@Getter
public class NewsfeedResponseDto {
    private String accountId;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    public NewsfeedResponseDto(Newsfeed newsfeed) {
        this.accountId = newsfeed.getUser().getAccountId();
        this.title = newsfeed.getTitle();
        this.description = newsfeed.getDescription();
        this.createdAt = newsfeed.getCreatedAt();
    }

    public static NewsfeedResponseDto toDto(Newsfeed newsfeed) {
        return new NewsfeedResponseDto(newsfeed);
    }
}