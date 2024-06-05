package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.Newsfeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {
}
