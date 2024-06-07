package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {
//    List<Newsfeed> findAllByOrderByCreatedAtDesc();
//
//    Optional<Newsfeed> findByIdAndUserId(Long id, Long id1);

    List<Newsfeed> findAllByUser_UserStatusOrderByCreatedAtDesc(UserStatus active);

    Optional<Newsfeed> findByIdAndUser_UserStatus(Long postId, UserStatus userStatus);
}