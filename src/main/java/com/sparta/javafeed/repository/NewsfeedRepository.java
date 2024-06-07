package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.Newsfeed;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {
    List<Newsfeed> findAllByOrderByCreatedAtDesc();

    Optional<Newsfeed> findByIdAndUserId(Long id, Long id1);

    List<Newsfeed> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}