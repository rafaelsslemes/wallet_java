package com.wallet.statement_ms.services;

import java.util.UUID;

import com.wallet.statement_ms.domain.Transaction;
import com.wallet.statement_ms.domain.errors.UniquenessViolation;

public interface TransactionServiceInterface {
    public void processPendings();
    public UUID process(Transaction transaction) throws UniquenessViolation;
}
