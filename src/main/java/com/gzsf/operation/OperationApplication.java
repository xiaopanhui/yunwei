package com.gzsf.operation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {})
@EnableScheduling
//@MapperScan("com.gzsf.operation.dao.*")
@EnableCaching
public class OperationApplication {
	public static void main(String[] args) {
		SpringApplication.run(OperationApplication.class, args);
	}
}
