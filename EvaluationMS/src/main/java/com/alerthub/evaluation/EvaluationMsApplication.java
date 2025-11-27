package com.alerthub.evaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.alerthub.evaluation.clients")
public class EvaluationMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvaluationMsApplication.class, args);
	}

}
