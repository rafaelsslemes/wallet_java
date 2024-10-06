package com.wallet.cashin_ms.service;

import java.util.UUID;

import com.wallet.cashin_ms.domain.CashIn;
import com.wallet.cashin_ms.domain.errors.UniquenessViolation;

public interface CashInServiceInterface {
    public void processPendings();
    public void sendProcessed();
    public UUID process(CashIn cashin) throws UniquenessViolation;
}
