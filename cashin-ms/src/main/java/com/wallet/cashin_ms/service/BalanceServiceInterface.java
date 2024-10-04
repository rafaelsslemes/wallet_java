package com.wallet.cashin_ms.service;

import com.wallet.cashin_ms.domain.CashIn;

public interface BalanceServiceInterface {
    
    public void sendBalanceUpdate(CashIn cashIn);
}
