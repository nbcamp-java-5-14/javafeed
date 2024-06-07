package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.CommentResponseDto;
import com.sparta.javafeed.entity.Comment;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
