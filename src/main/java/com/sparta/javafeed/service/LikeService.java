package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.LikeRequestDto;
import com.sparta.javafeed.dto.LikeResponseDto;
import com.sparta.javafeed.entity.Comment;
import com.sparta.javafeed.entity.Like;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import com.sparta.javafeed.enums.ContentType;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final NewsfeedService newsfeedService;
    private final CommentService commentService;

    @Transactional
    public void createLike(LikeRequestDto requestDto, User user) {
        // 유저 조회
        User userByAccountId = userService.findByAccountId(user.getAccountId());

        // 콘텐츠 조회(뉴스피드, 댓글)
        ContentType contentType = ContentType.toContentType(requestDto.getContentType().toUpperCase());
        if(contentType == ContentType.NEWSFEED){
            //뉴스피드 조회
            Newsfeed newsfeedById = newsfeedService.getNewsfeed(requestDto.getContentId());

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
        } else if(contentType == ContentType.COMMENT){
            // 댓글 조회
            Comment commentById = commentService.getComment(requestDto.getContentId());

            // 좋아요를 누른 유저와 콘텐츠를 적은 유저가 동일한지 확인
            if(userByAccountId.getId().equals(commentById.getUser().getId())){
                throw new CustomException(ErrorType.CANNOT_LIKE_OWN_CONTENT);
            }

            // 이미 좋아요 했는지 확인
            Optional<Like> byContentIdAndUserAndContentTypeLike = likeRepository.findByContentIdAndUserAndContentTypeLike(commentById.getId(), userByAccountId, ContentType.COMMENT);
            if(byContentIdAndUserAndContentTypeLike.isPresent()){
                throw new CustomException(ErrorType.ALREADY_LIKED);
            }

            Like like = new Like(userByAccountId, commentById.getId(), ContentType.COMMENT);
            commentById.increaseLikeCnt();
            likeRepository.save(like);
        }
    }

    @Transactional
    public void deleteLike(Long likeId, User user) {
        // 유저 조회
        User userByAccountId = userService.findByAccountId(user.getAccountId());

        // like 조회
        Like like = likeRepository.findById(likeId).orElseThrow(
                () -> new CustomException(ErrorType.NOT_FOUND_LIKE)
        );

        // 요청하는 유저와 좋아요 유저가 같은지 확인
        if(!like.getUser().getId().equals(userByAccountId.getId())){
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        // 댓글 좋아요 카운트 내리기
        if (like.getContentType() == ContentType.COMMENT){
            Comment comment = commentService.getComment(like.getContentId());
            comment.decreaseLikeCnt();
        }

        likeRepository.delete(like);
    }

    public List<LikeResponseDto> getLikes(Long contentId, String contentType) {
        List<Like> likeList = likeRepository.findByContentIdAndContentTypeLike(contentId, ContentType.toContentType(contentType));

        return likeList.stream().map(LikeResponseDto::new).toList();
    }
}
