package com.wallet.cashin_ms.infra.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class CashInDto implements Serializable {
    
    private UUID eventId;
    private UUID sourceId;
    private UUID payerId;
    private UUID receiverId;
    
    private double value;
    private Date date;
}
