package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import com.sparta.javafeed.dto.NewsfeedResponseDto;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserRole;
import com.sparta.javafeed.exception.UserException;
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
    public NewsfeedResponseDto save(NewsfeedRequestDto requestDto, User user) {
        Newsfeed newsfeed = newsfeedRepository.save(requestDto.toEntity(user));
        return NewsfeedResponseDto.toDto(newsfeed);
    }

    public List<NewsfeedResponseDto> getNewsfeed() {
        return newsfeedRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(NewsfeedResponseDto::new).toList();
    }

    @Transactional
    public Long updateNewsfeed(Long id, NewsfeedRequestDto requestDto, User user) {
        Newsfeed newsfeed = checkValidatedNewfeed(id, user);
        newsfeed.update(requestDto);
        return newsfeed.getId();

    }

    private Newsfeed checkValidatedNewfeed(Long id, User user) {
        Newsfeed newsfeed;

        if(user.getUserRole().equals(UserRole.ADMIN)) {
            newsfeed = newsfeedRepository.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        } else {
            newsfeed = newsfeedRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() ->
                    new UserException(ErrorType.NO_AUTHENTICATION));
        }

        return newsfeed;
    }
}