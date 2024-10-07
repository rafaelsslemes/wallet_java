package com.wallet.purchase_ms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.purchase_ms.domain.InBox;

public interface InBoxRepository extends JpaRepository<InBox, Long> {

    public List<InBox> findByProcessed(Boolean processed);
    
}
