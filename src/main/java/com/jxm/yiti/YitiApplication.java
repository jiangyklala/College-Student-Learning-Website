package com.jxm.yiti;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling
@MapperScan("com.jxm.yiti.mapper")
public class YitiApplication {

	public static void main(String[] args) {
		SpringApplication.run(YitiApplication.class, args);
	}

}
