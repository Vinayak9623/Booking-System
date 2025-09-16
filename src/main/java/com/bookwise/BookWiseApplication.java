package com.bookwise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BookWiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookWiseApplication.class, args);
        log.info("Welcome to BookWise Application");
	}

}
