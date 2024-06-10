package com.sparta.javafeed.enums;

import com.sparta.javafeed.exception.CustomException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Getter
public enum VideoFileType {

    MP4("video/mp4"),
    AVI("video/x-msvideo");

    private final String type;
    private static final Long maxSize = 200L * 1024L * 1024L; // 최대 용량 제한 200MB

    VideoFileType(String type) {
        this.type = type;
    }

    public static boolean isVideoFileType(String fileType) {
        return Arrays.stream(VideoFileType.values()).anyMatch(videoFileType -> videoFileType.type.equals(fileType));
    }

    public static void checkLimit(MultipartFile file) {
        if (file.getSize() > maxSize) {
            throw new CustomException(ErrorType.VIDEO_LIMIT_EXCEEDED);
        }
    }
}
