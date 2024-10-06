package com.wallet.balance_ms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.balance_ms.domain.Balance;
import com.wallet.balance_ms.domain.errors.InsuficientBalance;
import com.wallet.balance_ms.dto.BalanceDto;
import com.wallet.balance_ms.dto.UpdateBalanceDto;
import com.wallet.balance_ms.service.BalanceServiceInterface;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("balance")
@Slf4j
public class BalanceController {

    @Autowired
    private BalanceServiceInterface service;

    @PostMapping()
    public UUID createBalance(@RequestBody BalanceDto dto) {
        log.info("BALANCE RECEIVED: {}", dto.getAccountId());

        Balance saved = service.save(dto);

        log.info("BALANCE CREATED: {}", saved.getAccountId());
        return saved.getAccountId();
    }

    @GetMapping("/{id}")
    public String getBalance(@RequestParam String param) {
        return new String();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBalance (@RequestBody UpdateBalanceDto dto, @PathVariable UUID id){
        try {
            service.update(dto);   
            return new ResponseEntity<String>(HttpStatus.ACCEPTED);

        }
        catch (NotFoundException e){
            return new ResponseEntity<String>("BALANCE_NOT_FOUND", HttpStatus.NOT_FOUND);
        } catch (InsuficientBalance e) {
            return new ResponseEntity<String>("INSUFICIENT_BALANCE", HttpStatus.PRECONDITION_FAILED);
        }
    }
}