package com.aistudy.backend.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI(){


        return new OpenAPI()

                .info(
                        new Info()
                                .title("AI学习助手 API")
                                .description("AI学习助手后端接口文档")
                                .version("1.0")
                )


                // 全局JWT认证
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("JWT")
                )


                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "JWT",
                                        new SecurityScheme()

                                                // HTTP认证
                                                .type(
                                                    SecurityScheme.Type.HTTP
                                                )

                                                // Bearer Token
                                                .scheme(
                                                    "bearer"
                                                )

                                                // JWT格式
                                                .bearerFormat(
                                                    "JWT"
                                                )

                                                // 请求头名称
                                                .name(
                                                    "Authorization"
                                                )

                                )
                );


    }


}