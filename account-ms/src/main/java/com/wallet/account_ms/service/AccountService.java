package com.wallet.account_ms.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.wallet.account_ms.domain.Account;
import com.wallet.account_ms.domain.User;
import com.wallet.account_ms.dto.AccountDto;
import com.wallet.account_ms.repository.AccountRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService implements AccountServiceInterface {

    @Autowired
    AccountRepository repository;

    @Autowired
    UserServiceInterface userService;

    @Override
    @Transactional
    public Account create(AccountDto dto) throws BadRequestException {


        User newUser = new User(null, dto.getUsername(), dto.getPassword(), null);
        userService.create(newUser);

        Account newAccount = new Account(UUID.randomUUID(), newUser);
        repository.save(newAccount);

        return newAccount;
    }

    @Override
    public double getBalance(UUID accountId) throws NotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public double getStatement(UUID accountId, int page) throws NotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatement'");
    }



}
