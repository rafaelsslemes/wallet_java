package com.wallet.cashout_ms.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wallet.cashout_ms.service.CashOutServiceInterface;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
/// Processa periodicamente as mensangens armazenadas ao InBox
public class ProcessScheduled {
    @Autowired
    private CashOutServiceInterface cashOutService;
    
	@Scheduled(fixedRate = 10000)
	public void processReceived() {
        
		log.info("PROCESSING INBOX MESSAGES... {}", new Date());
        
        cashOutService.processPendings();

		log.info("MESSAGES PROCESSED. {}", new Date());

	}

    @Scheduled(fixedRate = 10000)
	public void sendProcessed() {
        
		log.info("PROCESSING OUTBOX MESSAGES... {}", new Date());
        
        cashOutService.sendProcessed();

		log.info("MESSAGES SENT. {}", new Date());

	}
}
    
