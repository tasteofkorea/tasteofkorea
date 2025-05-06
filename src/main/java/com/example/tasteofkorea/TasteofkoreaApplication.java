package com.example.tasteofkorea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TasteofkoreaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasteofkoreaApplication.class, args);
	}

}
