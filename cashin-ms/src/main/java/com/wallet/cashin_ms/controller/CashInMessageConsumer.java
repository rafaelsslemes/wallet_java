package com.wallet.cashin_ms.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.wallet.cashin_ms.dto.CashInDto;
import com.wallet.cashin_ms.service.CashInServiceInterface;
import com.wallet.cashin_ms.service.InBoxServiceInterface;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CashInMessageConsumer {

    @Autowired
    private InBoxServiceInterface inBoxService;

    @Autowired
    private CashInServiceInterface cashInService;

    @SneakyThrows
    @KafkaListener(topics = "cashin-topic", groupId = "cashIn-group", containerFactory = "jsonContainerFactory")
    public void receive(@Payload CashInDto cashIn) {

        log.info("CashIn received: {}", cashIn.getEventId());
        this.validatePayload(cashIn);

        // Armazena a mensagem recebida na InBox para processar posteriormente
        inBoxService.save(cashIn);
        log.info("CashIn saved InBox: {}", cashIn.getEventId());

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