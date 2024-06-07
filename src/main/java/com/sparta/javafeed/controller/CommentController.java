package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.CommentRequestDto;
import com.sparta.javafeed.dto.CommentResponseDto;
import com.sparta.javafeed.dto.ResponseEntityDto;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.security.CommentService;
import com.sparta.javafeed.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = commentService.addComment(userDetails.getUser(), postId, requestDto.getDescription());

        ResponseEntityDto<CommentResponseDto> responseEntity =
                new ResponseEntityDto<>(ResponseStatus.COMMENT_CREATE_SUCCESS, responseDto);

        return ResponseEntity.ok(responseEntity);
    }
}
