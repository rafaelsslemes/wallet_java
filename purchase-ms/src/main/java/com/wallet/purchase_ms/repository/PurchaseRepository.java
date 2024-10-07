package com.wallet.purchase_ms.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.purchase_ms.domain.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {  
    
    Optional<Purchase> findBySourceIdAndPayerIdAndReceiverIdAndValue(UUID sourceId, UUID payerId, UUID receiverId, double value);
}
