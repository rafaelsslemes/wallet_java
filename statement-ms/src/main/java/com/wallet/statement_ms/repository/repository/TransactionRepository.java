package com.wallet.statement_ms.repository.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.statement_ms.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {  
    
    Optional<List<Transaction>> findAllByReceiverId(UUID receiverId);
}
