package com.wallet.cashin_ms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.cashin_ms.domain.OutBox;

public interface OutBoxRepository extends JpaRepository<OutBox, Long> {

    public List<OutBox> findByProcessed(Boolean processed);
}
