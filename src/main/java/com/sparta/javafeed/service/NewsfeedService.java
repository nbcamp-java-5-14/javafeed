package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import com.sparta.javafeed.dto.NewsfeedResponseDto;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.repository.NewsfeedRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Page<NewsfeedResponseDto> getNewsfeed(int page) {

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(page, 10, sort);

        return newsfeedRepository.findAll(pageable).map(NewsfeedResponseDto::new);

//        return newsfeedRepository.findAllByOrderByCreatedAtDesc().stream()
//                .map(NewsfeedResponseDto::new).toList();
    }

    @Transactional
    public Long updateNewsfeed(Long id, NewsfeedRequestDto requestDto, User user) {
        Newsfeed newsfeed = checkValidatedNewsfeed(id, user);
        newsfeed.update(requestDto);
        return newsfeed.getId();

    }

    @Transactional
    public Long deleteNewsfeed(Long id, User user) {
        Newsfeed newsfeed = checkValidatedNewsfeed(id, user);
        newsfeedRepository.delete(newsfeed);
        return newsfeed.getId();
    }

    private Newsfeed checkValidatedNewsfeed(Long id, User user) {
        Newsfeed newsfeed = newsfeedRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUNT_POST));

        newsfeed.userValidate(user);

        return newsfeed;
    }
}