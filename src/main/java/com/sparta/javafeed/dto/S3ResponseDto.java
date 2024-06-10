package com.sparta.javafeed.dto;

import lombok.Getter;

@Getter
public class S3ResponseDto {

    private String originName;
    private String saveFileName;
    private String url;
    private Long size;

    public S3ResponseDto(String originName, String saveFileName, String url, long size) {
        this.originName = originName;
        this.saveFileName = saveFileName;
        this.url = url;
        this.size = size;
    }
}
