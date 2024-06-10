package com.sparta.javafeed.dto;

import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.entity.User;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class NewsfeedRequestDto {
    private String title;
    private String description;
    @Size(min = 1, max = 5, message = "최대 5개까지만 업로드 가능합니다.")
    private List<MultipartFile> files;

    public Newsfeed toEntity(User user) {
        return new Newsfeed(this.title, this.description, user);
    }
}
