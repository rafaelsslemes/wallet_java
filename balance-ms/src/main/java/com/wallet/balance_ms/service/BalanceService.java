package com.wallet.balance_ms.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.wallet.balance_ms.domain.Balance;
import com.wallet.balance_ms.domain.errors.InsuficientBalance;
import com.wallet.balance_ms.dto.BalanceDto;
import com.wallet.balance_ms.dto.UpdateBalanceDto;
import com.wallet.balance_ms.dto.UpdateBalanceType;
import com.wallet.balance_ms.repository.BalanceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BalanceService implements BalanceServiceInterface {

    @Autowired
    BalanceRepository repository;

    @Override
    public Balance save(BalanceDto dto) {

        Balance balance = new Balance(dto.getAccountId(), 0, new Date(), 0);

        repository.save(balance);

        return balance;
    }

    @Override
    public void update(UpdateBalanceDto dto) throws NotFoundException, InsuficientBalance {

        Optional<Balance> optional = repository.findById(dto.getReceiverId());
        if (optional.isPresent()) {

            Balance balance = optional.get();

            if (dto.getUpdateBalanceType() == UpdateBalanceType.CREDIT) {
                balance.setValue(balance.getValue() + dto.getValue());
            } else {

                if (balance.getValue() < dto.getValue()) {
                    throw new InsuficientBalance();
                }

                balance.setValue(balance.getValue() - dto.getValue());
            }

            try {
                repository.save(balance);
                return;

            } catch (ObjectOptimisticLockingFailureException e) {
                // Quando o registro está bloqueado, tenta novamente até conseguir
                this.update(dto);
            }

        }

        throw new NotFoundException();
    }

}
