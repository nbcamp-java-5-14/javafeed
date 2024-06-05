package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import com.sparta.javafeed.dto.NewsfeedResponseDto;
import com.sparta.javafeed.security.UserDetailsImpl;
import com.sparta.javafeed.service.NewsfeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class NewsfeedController {

    private final NewsfeedService newsfeedService;

    @PostMapping
    public NewsfeedResponseDto createNewsfeed(
            @Valid @RequestBody NewsfeedRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return newsfeedService.save(requestDto, userDetails.getUser());
    }

    @GetMapping
    public List<NewsfeedResponseDto> getNewsfeed() {
        return newsfeedService.getNewsfeed();
    }

    @PutMapping("/{id}")
    public Long updateNewsfeed(
            @PathVariable Long id,
            @RequestBody NewsfeedRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return newsfeedService.updateNewsfeed(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{id}")
    public Long deleteNewsfeed(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return newsfeedService.deleteNewsfeed(id, userDetails.getUser());
    }
}