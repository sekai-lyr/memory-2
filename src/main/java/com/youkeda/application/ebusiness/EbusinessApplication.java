package com.youkeda.application.ebusiness;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.youkeda.application.ebusiness.dao")
public class EbusinessApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbusinessApplication.class, args);
	}

}
