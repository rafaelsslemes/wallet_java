package com.wallet.statement_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StatementMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatementMsApplication.class, args);
	}

}
