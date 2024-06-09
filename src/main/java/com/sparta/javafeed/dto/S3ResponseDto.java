package com.sparta.javafeed.dto;

import lombok.Getter;

@Getter
public class S3ResponseDto {

    private String saveFileName;
    private String url;

    public S3ResponseDto(String saveFileName, String url) {
        this.saveFileName = saveFileName;
        this.url = url;
    }
}
