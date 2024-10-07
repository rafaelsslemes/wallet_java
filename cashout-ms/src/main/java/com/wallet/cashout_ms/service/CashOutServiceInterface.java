package com.wallet.cashout_ms.service;

import java.util.UUID;

import com.wallet.cashout_ms.domain.CashOut;
import com.wallet.cashout_ms.domain.errors.UniquenessViolation;

public interface CashOutServiceInterface {
    public void processPendings();
    public void sendProcessed();
    public UUID process(CashOut cashin) throws UniquenessViolation;
}
