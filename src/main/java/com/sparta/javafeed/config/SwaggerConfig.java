package com.sparta.javafeed.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("뉴스피드 프로젝트") // API의 제목
                .description("뉴스피드 프로젝트 과제 - 뉴스피드 API") // API에 대한 설명
                .version("0.0.1"); // API의 버전
    }
}