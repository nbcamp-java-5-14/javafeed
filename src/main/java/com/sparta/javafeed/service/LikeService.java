package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.LikeRequestDto;
import com.sparta.javafeed.entity.Like;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ContentType;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final NewsfeedService newsfeedService;

    public void createLike(LikeRequestDto requestDto, User user) {
        // 유저 조회
        User userByAccountId = userService.findByAccountId(user.getAccountId());

        // 콘텐츠 조회(뉴스피드, 댓글)
        ContentType contentType = ContentType.toContentType(requestDto.getContentType().toUpperCase());
        if(contentType == ContentType.NEWSFEED){
            //뉴스피드 조회
            Newsfeed newsfeedById = newsfeedService.findById(requestDto.getContentId());

            // 좋아요를 누른 유저와 콘텐츠를 적은 유저가 동일한지 확인
            if(userByAccountId.getId().equals(newsfeedById.getUser().getId())){
                throw new CustomException(ErrorType.CANNOT_LIKE_OWN_CONTENT);
            }

            // 이미 좋아요를 했는지 확인
            Optional<Like> byContentIdAndContentTypeLike = likeRepository.findByContentIdAndUserAndContentTypeLike(newsfeedById.getId(), userByAccountId ,ContentType.NEWSFEED);
            if(byContentIdAndContentTypeLike.isPresent()){
               throw new CustomException(ErrorType.ALREADY_LIKED);
           }

            Like like = new Like(userByAccountId, newsfeedById.getId(), ContentType.NEWSFEED);
            likeRepository.save(like);
        }
    }
}
