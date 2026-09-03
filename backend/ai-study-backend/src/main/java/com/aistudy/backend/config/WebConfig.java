package com.aistudy.backend.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {


        registry.addMapping("/**")


                // 前端地址
                .allowedOriginPatterns(
                        "http://localhost:5173"
                )


                // 请求方式
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )


                // 重点！！！
                // 显式允许 Authorization
                .allowedHeaders(
                        "Authorization",
                        "Content-Type"
                )


                // 允许token
                .allowCredentials(true);


    }


}