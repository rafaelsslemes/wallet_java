package com.wallet.statement_ms.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.wallet.statement_ms.domain.Transaction;
import com.wallet.statement_ms.domain.errors.UniquenessViolation;

public interface TransactionServiceInterface {
    public void processPendings();
    public List<Transaction> listPaginated(UUID accountId, int page) throws NotFoundException;
    public UUID process(Transaction transaction) throws UniquenessViolation;
}
