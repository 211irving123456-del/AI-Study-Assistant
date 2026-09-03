package com.aistudy.backend;

import org.springframework.boot.SpringApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
        "com.aistudy.backend.user.mapper",
        "com.aistudy.backend.study.mapper",
		"com.aistudy.backend.plan.mapper",
		"com.aistudy.backend.task.mapper",
		"com.aistudy.backend.chat.mapper"
})
public class AiStudyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiStudyBackendApplication.class, args);
	}

}
