package com.sparta.javafeed.repository;

import com.sparta.javafeed.dto.CommentResponseDto;
import com.sparta.javafeed.entity.Comment;
import com.sparta.javafeed.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // postId에 해당하는 게시글 userStatus 별로 댓글 목록 조회
    List<Comment> findAllByNewsfeedIdAndUser_UserStatus(Long postId, UserStatus userStatus);
}
