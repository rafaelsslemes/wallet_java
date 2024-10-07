package com.wallet.cashout_ms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.cashout_ms.domain.InBox;


public interface InBoxRepository extends JpaRepository<InBox, Long> {

    public List<InBox> findByProcessed(Boolean processed);
    
}
