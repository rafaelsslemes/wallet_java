package com.wallet.cashin_ms.controller;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.wallet.cashin_ms.dto.CashInDto;
import com.wallet.cashin_ms.service.OutBoxServiceInterface;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class TransactionMessageProducer {

    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    @Autowired
    private OutBoxServiceInterface outBoxService;

    @SneakyThrows
    public void sendCashIn(CashInDto dto, long outBoxId) {
        CompletableFuture<SendResult<String, Serializable>> future = kafkaTemplate.send("transactions-topic", dto);

        log.info("Sending CashIn {} to TransactionTopic", dto.getEventId());

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                outBoxService.complete(outBoxId);
                log.info("CashIn {} sent to TransactionTopic", dto.getEventId());
            }
        });

    }

}
