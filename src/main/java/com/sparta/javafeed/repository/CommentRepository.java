package com.sparta.javafeed.repository;

import com.sparta.javafeed.dto.CommentResponseDto;
import com.sparta.javafeed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByNewsfeedId(Long postId);
}
