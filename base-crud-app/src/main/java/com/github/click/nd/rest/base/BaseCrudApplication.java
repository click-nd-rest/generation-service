package com.github.click.nd.rest.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BaseCrudApplication {
	public static void main(String[] args) {
		SpringApplication.run(BaseCrudApplication.class, args);
	}
}
