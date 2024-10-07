package com.wallet.cashout_ms.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.cashout_ms.domain.CashOut;

public interface CashoutRepository extends JpaRepository<CashOut, UUID> {  
    
    Optional<CashOut> findBySourceIdAndPayerIdAndReceiverIdAndValue(UUID sourceId, UUID payerId, UUID receiverId, double value);
}
