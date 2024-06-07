package com.sparta.javafeed.dto;

import com.sparta.javafeed.entity.Like;
import com.sparta.javafeed.enums.ContentType;
import lombok.Getter;

@Getter
public class LikeResponseDto {
    private Long id;
    private Long contentId;
    private Long userId;
    private String username;
    private ContentType contentType;

    public LikeResponseDto(Like like) {
        this.id = like.getId();
        this.contentId = like.getContentId();
        this.contentType = like.getContentType();
        this.userId = like.getUser().getId();
        this.username = like.getUser().getName();
    }
}
