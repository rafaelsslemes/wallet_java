package com.wallet.account_ms.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.account_ms.domain.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {  

}
