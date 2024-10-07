package com.wallet.account_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.account_ms.domain.User;


public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
