package com.example.separation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.example.separation.mapper")
public class SeparationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeparationApplication.class, args);
	}
}
