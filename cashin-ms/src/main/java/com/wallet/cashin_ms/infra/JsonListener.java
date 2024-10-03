package com.wallet.cashin_ms.infra;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.wallet.cashin_ms.infra.dto.CashInDto;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JsonListener {
    
    @SneakyThrows
    @KafkaListener(topics = "cashin-topic", groupId = "cashIn-group", containerFactory = "jsonContainerFactory")
    public void receive(@Payload CashInDto cashIn){
        log.info("CashIn received: {}", cashIn.toString());
    
    }

}