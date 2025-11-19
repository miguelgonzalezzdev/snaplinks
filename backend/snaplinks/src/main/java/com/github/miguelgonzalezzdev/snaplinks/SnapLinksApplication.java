package com.github.miguelgonzalezzdev.snaplinks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SnapLinksApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnapLinksApplication.class, args);
	}

}
