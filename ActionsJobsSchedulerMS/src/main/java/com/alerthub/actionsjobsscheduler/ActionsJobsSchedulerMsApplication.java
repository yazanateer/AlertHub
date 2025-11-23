package com.alerthub.actionsjobsscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ActionsJobsSchedulerMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActionsJobsSchedulerMsApplication.class, args);
	}

}
