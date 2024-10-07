package com.wallet.account_ms.service;


import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.wallet.account_ms.domain.Account;
import com.wallet.account_ms.dto.AccountDto;


public interface AccountServiceInterface {

    public Account create(AccountDto dto) throws BadRequestException;
    public double getBalance(UUID accountId) throws NotFoundException;
    public double getStatement(UUID accountId, int page) throws NotFoundException;
}
