package com.alerthub.loader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoaderMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoaderMsApplication.class, args);
	}

}
