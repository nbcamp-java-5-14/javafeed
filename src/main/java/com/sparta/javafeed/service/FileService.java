package com.sparta.javafeed.service;

import com.sparta.javafeed.dto.S3ResponseDto;
import com.sparta.javafeed.entity.File;
import com.sparta.javafeed.entity.Newsfeed;
import com.sparta.javafeed.enums.ErrorType;
import com.sparta.javafeed.exception.CustomException;
import com.sparta.javafeed.repository.FileRepository;
import com.sparta.javafeed.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final S3Util s3Util;

    /**
     * 다중 파일 등록
     * @param newsfeed 게시글
     * @param files 파일 목록
     */
    public void saveFiles(Newsfeed newsfeed, List<MultipartFile> files) {
        if (CollectionUtils.isEmpty(files)) {
            return;
        }

        if (files.size() > 5) {
            throw new CustomException(ErrorType.FILE_COUNT_EXCEEDED);
        }

        List<File> fileList = uploadFiles(files).stream().map(File::new).toList();

        for (File file : fileList) {
            file.addNewsfeed(newsfeed);
        }

        fileRepository.saveAll(fileList);
    }

    /**
     * AWS S3 다중 파일 업로드
     * @param files 파일 목록
     * @return 파일 업로드 정보 목록
     */
    private List<S3ResponseDto> uploadFiles(List<MultipartFile> files) {
        List<S3ResponseDto> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            fileList.add(s3Util.uploadFile(file, "postFile"));
        }
        return fileList;
    }

    /**
     * 다중 파일 삭제
     * @param fileList 파일 목록
     */
    public void deleteFiles(List<File> fileList) {
        if (CollectionUtils.isEmpty(fileList)) {
            return;
        }

        for (File file : fileList) {
            s3Util.deleteFile(file.getSaveName());
        }

        fileRepository.deleteAll(fileList);
    }
}
