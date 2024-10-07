package com.wallet.cashout_ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.wallet.cashout_ms.service.InBoxServiceInterface;
import com.wallet.cashout_ms.dto.CashOutDto;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CashOutMessageConsumer {

    @Autowired
    private InBoxServiceInterface inBoxService;

    @SneakyThrows
    @KafkaListener(topics = "cashout-topic", groupId = "cashOut-group", containerFactory = "jsonContainerFactory")
    public void receive(@Payload CashOutDto cashOut) {

        log.info("CashOut received: {}", cashOut.getEventId());
        this.validatePayload(cashOut);

        // Armazena a mensagem recebida na InBox para processar posteriormente
        inBoxService.save(cashOut);
        log.info("CashOut saved InBox: {}", cashOut.getEventId());

    }

    private void validatePayload(CashOutDto cashOut) {
        // TODO: Replace with Stream
        if (cashOut.getEventId() == null
                || cashOut.getSourceId() == null
                || cashOut.getPayerId() == null
                || cashOut.getReceiverId() == null
                || cashOut.getValue() == 0
                || cashOut.getDate() == null
        ) {
            log.error("cashOut {} has an invalid payload. Discarded.", cashOut.getEventId());
            
            // TODO: send to an Error Handling Topic
        }
        
        log.info("cashOut {} validated...", cashOut.getEventId());

    }
}