package com.sparta.javafeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용을 작성해주세요.")
    @Size(max = 200, message = "댓글 내용은 최대 200자 이내로 작성해주세요.")
    private String description;
}
