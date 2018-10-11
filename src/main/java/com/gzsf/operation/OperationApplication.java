package com.gzsf.operation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.gzsf.operation.dao.*")
public class OperationApplication {
	public static void main(String[] args) {
		SpringApplication.run(OperationApplication.class, args);
	}
}
