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
import com.wallet.account_ms.dto.BalanceDto;
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

    @Autowired
    BalanceServiceInterface balanceService;

    @Override
    @Transactional
    public Account create(AccountDto dto) throws BadRequestException {

        // Cria usuário
        User newUser = new User(null, dto.getUsername(), dto.getPassword(), null);
        userService.create(newUser);

        // Cria conta
        Account newAccount = new Account(UUID.randomUUID(), newUser);
        Account created = repository.save(newAccount);

        // Envia chamada para criar controlador de saldo
        requestBalance(created.getId());

        return created;
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

    private void requestBalance(UUID accountId) {
        // Integra com o serviço que controla o saldo
        BalanceDto dto = new BalanceDto(accountId, 0);

        try {
            balanceService.createBalance(dto);
        } catch (Exception e) {

            log.error("ERROR CREATING BALANCE!");
            throw new RuntimeException();
        }
    }


}
