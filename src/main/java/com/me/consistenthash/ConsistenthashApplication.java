package com.me.consistenthash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConsistenthashApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsistenthashApplication.class, args);
	}

}
