package com.sparta.javafeed.repository;

import com.sparta.javafeed.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
