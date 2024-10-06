package com.wallet.balance_ms.service;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.wallet.balance_ms.domain.Balance;
import com.wallet.balance_ms.domain.errors.InsuficientBalance;
import com.wallet.balance_ms.dto.BalanceDto;
import com.wallet.balance_ms.dto.UpdateBalanceDto;

public interface BalanceServiceInterface {

    public Balance save(BalanceDto dto);
    public void update(UpdateBalanceDto dto) throws NotFoundException, InsuficientBalance ;
}
