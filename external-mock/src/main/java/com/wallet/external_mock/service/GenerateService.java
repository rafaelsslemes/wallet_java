package com.wallet.external_mock.service;

    

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.wallet.external_mock.infra.dto.CashInDto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class GenerateService {

    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    @SneakyThrows
    public void sendCashIn(UUID accountId) {

        CashInDto dto = new CashInDto();
        dto.setDate(new Date());
        dto.setEventId(UUID.randomUUID());
        dto.setSourceId(UUID.randomUUID());
        dto.setPayerId(UUID.randomUUID());
        dto.setReceiverId(accountId); // point to user
        dto.setDetails("TEST PAYLOAD");
        dto.setValue(100);
               
        log.info("Sending Payment");
         
        kafkaTemplate.send("cashin-topic", dto);
    }
    
}
