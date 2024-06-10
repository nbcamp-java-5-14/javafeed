package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.CommentRequestDto;
import com.sparta.javafeed.dto.CommentResponseDto;
import com.sparta.javafeed.dto.ResponseEntityDto;
import com.sparta.javafeed.dto.ResponseStatusDto;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.service.CommentService;
import com.sparta.javafeed.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     * @param postId 게시글 ID
     * @param requestDto 댓글 내용
     * @param userDetails 회원 정보
     * @return 댓글 정보 및 응답 상태
     */
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

    /**
     * 댓글 목록 조회
     * @param postId 게시글 ID
     * @return 댓글 목록
     */
    @GetMapping("/{postId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    /**
     * 댓글 수정
     * @param commentId 댓글 ID
     * @param requestDto 수정 내용
     * @param userDetails 회원 정보
     * @return 댓글 정보 및 응답 상태
     */
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = commentService.updateComment(commentId, requestDto, userDetails.getUser());

        ResponseEntityDto<CommentResponseDto> responseEntity =
                new ResponseEntityDto<>(ResponseStatus.COMMENT_UPDATE_SUCCESS, responseDto);

        return ResponseEntity.ok(responseEntity);
    }

    /**
     * 댓글 삭제
     * @param commentId 댓글 ID
     * @param userDetails 회원 정보
     * @return 응답 상태
     */
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        ResponseStatusDto responseStatu = new ResponseStatusDto(ResponseStatus.COMMENT_DELETE_SUCCESS);

        return ResponseEntity.ok(responseStatu);
    }
}
