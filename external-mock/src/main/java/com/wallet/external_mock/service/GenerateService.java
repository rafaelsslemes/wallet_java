package com.wallet.external_mock.service;

    

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.wallet.external_mock.infra.dto.CashInDto;
import com.wallet.external_mock.infra.dto.CashOutDto;
import com.wallet.external_mock.infra.dto.PurchaseDto;
import com.wallet.external_mock.infra.dto.RefundDto;

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

    @SneakyThrows
    public void sendCashOut(UUID accountId) {
        CashOutDto dto = new CashOutDto();
        dto.setDate(new Date());
        dto.setEventId(UUID.randomUUID());
        dto.setSourceId(UUID.randomUUID());
        dto.setPayerId(UUID.randomUUID());
        dto.setReceiverId(accountId); // point to user
        dto.setDetails("TEST PAYLOAD CASHOUT");
        dto.setValue(50);
               
        log.info("Sending Cashout");
         
        kafkaTemplate.send("cashout-topic", dto);
     }

     @SneakyThrows
     public void sendPurchase(UUID accountId) {
         PurchaseDto dto = new PurchaseDto();
         dto.setDate(new Date());
         dto.setEventId(UUID.randomUUID());
         dto.setSourceId(UUID.randomUUID());
         dto.setPayerId(UUID.randomUUID());
         dto.setReceiverId(accountId); // point to user
         dto.setDetails("TEST PAYLOAD PURCHASE");
         dto.setSellerDetails("SELLER DETAILS");
         dto.setValue(50);
                
         log.info("Sending PURCHASE");
          
         kafkaTemplate.send("purchase-topic", dto);
      }

      @SneakyThrows
     public void sendRefund(UUID purchaseId) {
         RefundDto dto = new RefundDto();
         
         dto.setPurchaseId(purchaseId);
         log.info("Sending REFUND");
          
         kafkaTemplate.send("refund-topic", dto);
      }
    
}
