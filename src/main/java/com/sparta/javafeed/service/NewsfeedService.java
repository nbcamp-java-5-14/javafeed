package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.NewsfeedRequestDto;
import com.sparta.javafeed.dto.NewsfeedResponseDto;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.enums.UserStatus;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.repository.NewsfeedRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    public Page<NewsfeedResponseDto> getNewsfeed(int page, String searchStartDate, String searchEndDate) {

        if(searchStartDate == null) {
            searchStartDate = "00010101";
        }
        if(searchEndDate == null) {
            searchEndDate = "99991231";
        }

        LocalDateTime startDateTime = LocalDate.parse(searchStartDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(0, 0, 0);
        LocalDateTime endDateTime = LocalDate.parse(searchEndDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atTime(23, 59, 59);

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(page, 10, sort);

        return new PageImpl<>(newsfeedRepository.findAllByCreatedAtBetweenAndUser_UserStatus(startDateTime, endDateTime, pageable, UserStatus.ACTIVE)
                .stream().map(NewsfeedResponseDto::new).collect(Collectors.toList()));
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
                new CustomException(ErrorType.NOT_FOUND_POST));

        newsfeed.userValidate(user);

        return newsfeed;
    }

    public Newsfeed getNewsfeed(Long postId) {
        return newsfeedRepository.findByIdAndUser_UserStatus(postId, UserStatus.ACTIVE)
                .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_POST));
    }
}