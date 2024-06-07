package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

    List<Newsfeed> findAllByCreatedAtBetweenAndUser_UserStatus(LocalDateTime start, LocalDateTime end, Pageable pageable, UserStatus active);

    Optional<Newsfeed> findByIdAndUser_UserStatus(Long postId, UserStatus userStatus);
}