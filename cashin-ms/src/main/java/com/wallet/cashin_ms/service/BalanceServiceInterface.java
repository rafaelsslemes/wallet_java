package com.wallet.cashin_ms.service;

import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wallet.cashin_ms.dto.UpdateBalanceDto;

@FeignClient(name = "balance-ms", url = "http://localhost:8002", path = "/balance")
public interface BalanceServiceInterface {
    
    @PutMapping("/{id}")
    public ResponseEntity<String> sendBalanceUpdate(@RequestBody UpdateBalanceDto dto, @PathVariable("id") UUID receiverId);
}