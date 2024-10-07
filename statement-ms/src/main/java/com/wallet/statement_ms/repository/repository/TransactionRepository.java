package com.wallet.statement_ms.repository.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wallet.statement_ms.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<List<Transaction>> findAllByReceiverId(UUID receiverId);

    @Query(value = "SELECT * FROM transaction t WHERE t.receiver_id = :receiverId ORDER BY t.date DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    Optional<List<Transaction>> findAllByReceiverIdAndMore(UUID receiverId, int offset, int limit);
}
