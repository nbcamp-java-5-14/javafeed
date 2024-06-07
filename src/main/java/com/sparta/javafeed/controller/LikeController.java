package com.sparta.javafeed.controller;

import com.fasterxml.jackson.databind.deser.BasicDeserializerFactory;
import com.sparta.javafeed.dto.LikeRequestDto;
import com.sparta.javafeed.dto.ResponseStatusDto;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ResponseStatus;
import com.sparta.javafeed.security.UserDetailsImpl;
import com.sparta.javafeed.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<?> createLike(@RequestBody @Valid LikeRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        likeService.createLike(requestDto, userDetails.getUser());

        return ResponseEntity.ok(new ResponseStatusDto(ResponseStatus.LIKE_CREATE_SUCCESS));
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<?> deleteLike(@PathVariable Long likeId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        likeService.deleteLike(likeId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseStatusDto(ResponseStatus.LIKE_DELETE_SUCCESS));
    }
}
