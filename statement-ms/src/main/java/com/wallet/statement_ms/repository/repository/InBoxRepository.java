package com.wallet.statement_ms.repository.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.statement_ms.domain.InBox;


public interface InBoxRepository extends JpaRepository<InBox, Long> {

    public List<InBox> findByProcessed(Boolean processed);
    
}
