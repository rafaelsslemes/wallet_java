package com.wallet.purchase_ms.service;

import java.util.UUID;

import com.wallet.purchase_ms.domain.Purchase;
import com.wallet.purchase_ms.domain.errors.UniquenessViolation;

public interface PurchaseServiceInterface {
    public void processPendings();
    public void sendProcessed();
    public UUID process(Purchase cashin) throws UniquenessViolation;
}
