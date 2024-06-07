package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
