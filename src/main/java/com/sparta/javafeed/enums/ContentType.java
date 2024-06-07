package com.sparta.javafeed.enums;

import com.sparta.javafeed.exception.CustomException;

public enum ContentType {
    NEWSFEED, COMMENT;


    public static ContentType toContentType(String string){
        ContentType contentType;
        try{
            contentType = ContentType.valueOf(string);
        } catch (IllegalArgumentException e){
            throw new CustomException(ErrorType.NON_EXISTENT_CONTENT_TYPE);
        }
        return contentType;
    }
}
