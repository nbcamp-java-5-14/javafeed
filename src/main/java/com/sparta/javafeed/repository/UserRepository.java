package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {
}
