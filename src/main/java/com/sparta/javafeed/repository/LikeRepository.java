package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.Like;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByContentIdAndUserAndContentTypeLike(Long id, User user, ContentType contentType);

    List<Like> findByContentIdAndContentTypeLike(Long contentId, ContentType contentType);
}
