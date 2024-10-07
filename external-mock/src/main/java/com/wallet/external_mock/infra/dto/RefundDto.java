package com.wallet.external_mock.infra.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class RefundDto implements Serializable {
    
    private UUID purchaseId; // Controle de idempotência
}
