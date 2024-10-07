package com.wallet.statement_ms.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wallet.statement_ms.services.TransactionService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
/// Processa periodicamente as menssagens armazenadas ao InBox
public class ProcessScheduled {
    @Autowired
    private TransactionService transactionService;
    
	@Scheduled(fixedRate = 10000)
	public void processReceived() {
        
		log.info("PROCESSING INBOX MESSAGES... {}", new Date());
        
        transactionService.processPendings();

		log.info("MESSAGES PROCESSED. {}", new Date());

	}

}
    
