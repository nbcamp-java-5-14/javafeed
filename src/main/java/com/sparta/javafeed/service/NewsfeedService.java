package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import com.sparta.javafeed.dto.NewsfeedResponseDto;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.repository.NewsfeedRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class NewsfeedService {

    private final NewsfeedRepository newsfeedRepository;

    @Transactional
    public NewsfeedResponseDto save(NewsfeedRequestDto requestDto) {
        Newsfeed newsfeed = newsfeedRepository.save(requestDto.toEntity());
        return NewsfeedResponseDto.toDto(newsfeed);
    }

    public List<NewsfeedResponseDto> getNewsfeed() {
        return newsfeedRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(NewsfeedResponseDto::new).toList();
    }
}