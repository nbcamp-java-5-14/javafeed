package com.sparta.javafeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LikeRequestDto {
    @NotNull
    private Long contentId;
    @NotBlank
    private String contentType;
}
