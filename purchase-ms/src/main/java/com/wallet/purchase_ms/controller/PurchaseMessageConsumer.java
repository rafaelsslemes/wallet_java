package com.wallet.purchase_ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.wallet.purchase_ms.dto.PurchaseDto;
import com.wallet.purchase_ms.service.InBoxServiceInterface;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class PurchaseMessageConsumer {

    @Autowired
    private InBoxServiceInterface inBoxService;

    @SneakyThrows
    @KafkaListener(topics = "purchase-topic", groupId = "purchase-group", containerFactory = "jsonContainerFactory")
    public void receive(@Payload PurchaseDto purchase) {

        log.info("Purchase received: {}", purchase.getEventId());
        this.validatePayload(purchase);

        // Armazena a mensagem recebida na InBox para processar posteriormente
        inBoxService.save(purchase);
        log.info("Purchase saved InBox: {}", purchase.getEventId());

    }

    private void validatePayload(PurchaseDto purchase) {
        // TODO: Replace with Stream
        if (purchase.getEventId() == null
                || purchase.getSourceId() == null
                || purchase.getPayerId() == null
                || purchase.getReceiverId() == null
                || purchase.getValue() == 0
                || purchase.getDate() == null
        ) {
            log.error("Purchase {} has an invalid payload. Discarded.", purchase.getEventId());
            
            // TODO: send to an Error Handling Topic
        }
        
        log.info("Purchase {} validated...", purchase.getEventId());

    }
}