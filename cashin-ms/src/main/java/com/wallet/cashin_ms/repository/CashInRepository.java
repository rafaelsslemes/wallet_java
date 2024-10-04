package com.wallet.cashin_ms.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.cashin_ms.domain.CashIn;

public interface CashInRepository extends JpaRepository<CashIn, UUID> {  
    
    Optional<CashIn> findBySourceIdAndPayerIdAndReceiverIdAndValue(UUID sourceId, UUID payerId, UUID receiverId, double value);
}
