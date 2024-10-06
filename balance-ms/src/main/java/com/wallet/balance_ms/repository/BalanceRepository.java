package com.wallet.balance_ms.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.balance_ms.domain.Balance;


public interface BalanceRepository extends JpaRepository<Balance, UUID> {  

}
