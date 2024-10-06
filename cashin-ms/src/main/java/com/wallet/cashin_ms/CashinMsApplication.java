package com.wallet.cashin_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class CashinMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashinMsApplication.class, args);
	}

}
