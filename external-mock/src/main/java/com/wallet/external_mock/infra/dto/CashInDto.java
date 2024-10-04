package com.wallet.external_mock.infra.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class CashInDto implements Serializable{
    
    private UUID eventId; // Controle de idempotência
    private UUID sourceId; // Qual a instituição que enviou
    private UUID payerId; // Conta de quem pagou
    private UUID receiverId; // Conta de quem recebeu
    private String details;
    
    private double value;
    private Date date;
}
