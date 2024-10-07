package com.wallet.balance_ms.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceDto {
    
    private UUID accountId;
    private double value;
}
