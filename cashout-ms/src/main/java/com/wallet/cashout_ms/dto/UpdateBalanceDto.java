package com.wallet.cashout_ms.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateBalanceDto {
    
    private UUID receiverId;
    private UpdateBalanceType updateBalanceType;
    private double value;
}
