package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByAccountId(String accountId);

    Optional<User> findByEmail(String email);
}

