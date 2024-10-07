package com.wallet.purchase_ms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.purchase_ms.domain.OutBox;

public interface OutBoxRepository extends JpaRepository<OutBox, Long> {

    public List<OutBox> findByProcessed(Boolean processed);
}
