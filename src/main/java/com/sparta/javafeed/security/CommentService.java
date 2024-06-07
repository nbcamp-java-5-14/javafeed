package com.sparta.javafeed.security;

import com.sparta.javafeed.dto.CommentResponseDto;
import com.sparta.javafeed.entity.Comment;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.repository.CommentRepository;
import com.sparta.javafeed.service.NewsfeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
