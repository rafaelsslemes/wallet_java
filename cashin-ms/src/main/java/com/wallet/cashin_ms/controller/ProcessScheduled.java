package com.wallet.cashin_ms.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wallet.cashin_ms.service.CashInServiceInterface;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
/// Processa periodicamente as mensangens armazenadas ao InBox
public class ProcessScheduled {
    @Autowired
    private CashInServiceInterface cashInService;
    
	@Scheduled(fixedRate = 10000)
	public void processReceived() {
        
		log.info("PROCESSING INBOX MESSAGES... {}", new Date());
        
        cashInService.processPendings();

		log.info("MESSAGES PROCESSED. {}", new Date());

	}

    @Scheduled(fixedRate = 10000)
	public void sendProcessed() {
        
		log.info("PROCESSING OUTBOX MESSAGES... {}", new Date());
        
        cashInService.sendProcessed();

		log.info("MESSAGES SENT. {}", new Date());

	}
}
    
