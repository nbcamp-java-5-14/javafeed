package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
