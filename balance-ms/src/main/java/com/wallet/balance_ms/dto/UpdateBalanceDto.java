package com.wallet.balance_ms.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class UpdateBalanceDto {
    
    private UUID receiverId;
    private UpdateBalanceType updateBalanceType;
    private double value;
}
