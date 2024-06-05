package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import com.sparta.javafeed.dto.NewsfeedResponseDto;
import com.sparta.javafeed.service.NewsfeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class NewsfeedController {

    private final NewsfeedService newsfeedService;

    @PostMapping
    public NewsfeedResponseDto createNewsfeed(
            @Valid @RequestBody NewsfeedRequestDto requestDto) {

        return newsfeedService.save(requestDto);
    }

    @GetMapping
    public List<NewsfeedResponseDto> getNewsfeed() {
        return newsfeedService.getNewsfeed();
    }
}