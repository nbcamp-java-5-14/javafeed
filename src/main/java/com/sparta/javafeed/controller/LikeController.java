package com.sparta.javafeed.controller;

import com.fasterxml.jackson.databind.deser.BasicDeserializerFactory;
import com.sparta.javafeed.dto.LikeRequestDto;
import com.sparta.javafeed.dto.LikeResponseDto;
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

import java.util.List;

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

    // 임시로 구현한 조회
    // 콘텐츠 타입과 아이디로 해당 콘텐츠의 좋아요 전체 조회
    @GetMapping("/{contentId}/{contentType}")
    public List<LikeResponseDto> getLikes(@PathVariable Long contentId, @PathVariable String contentType){
        return likeService.getLikes(contentId, contentType);
    }
}
