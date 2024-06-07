package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.CommentRequestDto;
import com.sparta.javafeed.dto.CommentResponseDto;
import com.sparta.javafeed.entity.Comment;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserStatus;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final NewsfeedService newsfeedService;

    /**
     * 댓글 등록
     * @param user 회원 정보
     * @param postId 게시글 ID
     * @param description 댓글 내용
     * @return 댓글 정보
     */
    public CommentResponseDto addComment(User user, Long postId, String description) {
        Newsfeed newsfeed = newsfeedService.getNewsfeed(postId);

        Comment comment = new Comment(user, newsfeed, description);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    /**
     * 댓글 목록 조회
     * @param postId 게시글 ID
     * @return 댓글 목록
     */
    public List<CommentResponseDto> getComments(Long postId) {
        List<CommentResponseDto> commentList = commentRepository
                .findAllByNewsfeedIdAndUser_UserStatus(postId, UserStatus.ACTIVE).stream().map(CommentResponseDto::new).toList();

        if (commentList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_COMMENT);
        }

        return commentList;
    }

    /**
     * 댓글 수정
     * @param commentId 댓글 ID
     * @param requestDto 수정 내용
     * @param user 회원 정보
     * @return 댓글 정보
     */
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = getValidatedComment(commentId, user);

        comment.update(requestDto.getDescription());

        return new CommentResponseDto(comment);
    }

    /**
     * 댓글 삭제
     * @param commentId 댓글 ID
     * @param user 회원 정보
     */
    public void deleteComment(Long commentId, User user) {
        Comment comment = getValidatedComment(commentId, user);
        commentRepository.delete(comment);
    }

    /**
     * 댓글 조회 및 권한 검증
     * @param commentId 댓글 ID
     * @param user 회원 정보
     * @return 댓글 Entity
     */
    private Comment getValidatedComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_COMMENT));

        newsfeedService.getNewsfeed(comment.getNewsfeed().getId());

        comment.validate(user);

        return comment;
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                ()-> new CustomException(ErrorType.NOT_FOUND_COMMENT)
        );
    }
}
