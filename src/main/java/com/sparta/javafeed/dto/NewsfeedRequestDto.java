package com.sparta.javafeed.dto;

import com.sparta.javafeed.entity.Newsfeed;

public class NewsfeedRequestDto {
    private String title;
    private String description;

    public Newsfeed toEntity() {
        return new Newsfeed(this.title, this.description);
    }
}
