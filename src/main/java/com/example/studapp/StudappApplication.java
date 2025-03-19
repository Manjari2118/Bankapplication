package com.example.studapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudappApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudappApplication.class, args);
	}

}
