package com.sparta.javafeed.dto;

import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.repository.NewsfeedRepository;

import java.time.LocalDateTime;

public class NewsfeedResponseDto {
    private String accountId;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    public NewsfeedResponseDto(String accountId, String title, String description, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static NewsfeedResponseDto toDto(Newsfeed newsfeed) {
        return new NewsfeedResponseDto(newsfeed.getUser().getAccountId(), newsfeed.getTitle(),
                newsfeed.getDescription(), newsfeed.getCreatedAt());
    }
}