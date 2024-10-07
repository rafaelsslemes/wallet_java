package com.wallet.statement_ms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.wallet.statement_ms.dto.TransactionDto;
import com.wallet.statement_ms.services.InBoxServiceInterface;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class TransactionsMessageConsumer {

    @Autowired
    private InBoxServiceInterface inBoxService;


    @SneakyThrows
    @KafkaListener(topics = "transactions-topic", groupId = "statement-group", containerFactory = "jsonContainerFactory")
    public void receive(@Payload TransactionDto dto) {

        log.info("Transaction received: {}", dto.getEventId());

        // Armazena a mensagem recebida na InBox para processar posteriormente
        inBoxService.save(dto);
        log.info("Transaction saved InBox: {}", dto.getEventId());

    }
}