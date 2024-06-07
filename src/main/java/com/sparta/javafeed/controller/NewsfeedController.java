package com.sparta.javafeed.controller;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import com.sparta.javafeed.dto.NewsfeedResponseDto;
import com.sparta.javafeed.dto.ResponseEntityDto;
import com.sparta.javafeed.dto.ResponseStatusDto;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.security.UserDetailsImpl;
import com.sparta.javafeed.service.NewsfeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<NewsfeedResponseDto> getNewsfeed
            (@RequestParam(value = "page") int page,
             @RequestParam(value = "searchStartDate", required = false) String searchStartDate,
             @RequestParam(value = "searchEndDate", required = false) String searchEndDate
            ) {
        return newsfeedService.getNewsfeed(page-1, searchStartDate, searchEndDate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNewsfeed(
            @PathVariable Long id,
            @RequestBody NewsfeedRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long responseId = newsfeedService.updateNewsfeed(id, requestDto, userDetails.getUser());
        ResponseStatusDto responseEntity = new ResponseStatusDto(ResponseStatus.POST_UPDATE_SUCCESS);
        return ResponseEntity.ok(responseEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNewsfeed(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long responseId = newsfeedService.deleteNewsfeed(id, userDetails.getUser());
        ResponseStatusDto responseEntity = new ResponseStatusDto(ResponseStatus.POST_DELETE_SUCCESS);
        return ResponseEntity.ok(responseEntity);
    }
}