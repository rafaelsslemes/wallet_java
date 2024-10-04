package com.wallet.cashin_ms.controller;

import org.bouncycastle.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.cashin_ms.dto.CashInDto;
import com.wallet.cashin_ms.service.InBoxServiceInterface;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CashInMessageController {

    @Autowired
    private InBoxServiceInterface service;

    @SneakyThrows
    @KafkaListener(topics = "cashin-topic", groupId = "cashIn-group", containerFactory = "jsonContainerFactory")
    public void receive(@Payload CashInDto cashIn) {

        log.info("CashIn received: {}", cashIn.toString());
        this.validatePayload(cashIn);

        service.save(cashIn);

        // Recebeu evento
        // Validar dados
        // Armazenar no InBox
        // Processar InBox
        // Validar idempotencia
        // Validar duplicidade
        // Ao enviar para Balance trabalhar com concorrrência

    }

    private void validatePayload(CashInDto cashIn) {
        // TODO: Replace with Stream
        if (cashIn.getEventId() == null
                || cashIn.getSourceId() == null
                || cashIn.getPayerId() == null
                || cashIn.getReceiverId() == null
                || cashIn.getValue() == 0
                || cashIn.getDate() == null
        ) {
            log.error("CashIn {} has an invalid payload. Discarded.", cashIn.getEventId());
            
            // TODO: send to an Error Handling Topic
        }
        
        log.info("CashIn {} validated...", cashIn.getEventId());

    }

}