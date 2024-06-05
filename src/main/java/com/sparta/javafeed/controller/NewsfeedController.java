package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import com.sparta.javafeed.dto.NewsfeedResponseDto;
import com.sparta.javafeed.dto.ResponseEntityDto;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.security.UserDetailsImpl;
import com.sparta.javafeed.service.NewsfeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class NewsfeedController {

    private final NewsfeedService newsfeedService;

    @PostMapping
    public ResponseEntity<?> createNewsfeed(
            @Valid @RequestBody NewsfeedRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        NewsfeedResponseDto responseDto = newsfeedService.save(requestDto, userDetails.getUser());
        ResponseEntityDto<NewsfeedResponseDto> responseEntity = new ResponseEntityDto<>(ResponseStatus.POST_CREATE_SUCCESS, responseDto);
        return ResponseEntity.ok(responseEntity);
    }

    @GetMapping
    public List<NewsfeedResponseDto> getNewsfeed() {
        return newsfeedService.getNewsfeed();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNewsfeed(
            @PathVariable Long id,
            @RequestBody NewsfeedRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long responseId = newsfeedService.updateNewsfeed(id, requestDto, userDetails.getUser());
        ResponseEntityDto<Long> responseEntity = new ResponseEntityDto<>(ResponseStatus.POST_UPDATE_SUCCESS, responseId);
        return ResponseEntity.ok(responseEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNewsfeed(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long responseId = newsfeedService.deleteNewsfeed(id, userDetails.getUser());
        ResponseEntityDto<Long> responseEntity = new ResponseEntityDto<>(ResponseStatus.POST_DELETE_SUCCESS, responseId);
        return ResponseEntity.ok(responseEntity);;
    }
}