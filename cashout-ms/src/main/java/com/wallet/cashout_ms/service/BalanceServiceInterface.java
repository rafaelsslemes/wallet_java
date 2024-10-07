package com.wallet.cashout_ms.service;


import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wallet.cashout_ms.dto.UpdateBalanceDto;


@FeignClient(name = "balance-ms", url = "http://localhost:8002", path = "/balance")
public interface BalanceServiceInterface {
    
    @PutMapping("/{id}")
    public ResponseEntity<String> sendBalanceUpdate(@RequestBody UpdateBalanceDto dto, @PathVariable("id") UUID receiverId);
}