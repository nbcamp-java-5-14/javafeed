package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.CommentRequestDto;
import com.sparta.javafeed.dto.CommentResponseDto;
import com.sparta.javafeed.entity.Comment;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NewsfeedService newsfeedService;

    public CommentResponseDto addComment(User user, Long postId, String description) {
        Newsfeed newsfeed = newsfeedService.getNewsfeed(postId);
        Comment comment = new Comment(user, newsfeed, description);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getComments(Long postId) {
        return commentRepository.findAllByNewsfeedId(postId).stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = getValidatedComment(commentId, user);

        comment.update(requestDto.getDescription());

        return new CommentResponseDto(comment);
    }

    private Comment getValidatedComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new CustomException(ErrorType.NOT_FOUND_COMMENT));

        comment.validate(user);

        return comment;
    }
}
