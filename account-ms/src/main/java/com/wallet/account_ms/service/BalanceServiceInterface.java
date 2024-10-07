package com.wallet.account_ms.service;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.wallet.account_ms.dto.BalanceDto;


@FeignClient(name = "balance-ms", url = "http://localhost:8002", path = "/balance")
public interface BalanceServiceInterface {
    
    @GetMapping("/{id}")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable UUID id);
}