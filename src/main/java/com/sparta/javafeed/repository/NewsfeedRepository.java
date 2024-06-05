package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.Newsfeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {
    List<Newsfeed> findAllByOrderByCreatedAtDesc();

    Optional<Newsfeed> findByIdAndUserId(Long id, Long id1);
}