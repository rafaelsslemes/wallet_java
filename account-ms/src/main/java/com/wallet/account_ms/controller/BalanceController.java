package com.wallet.account_ms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.account_ms.dto.BalanceDto;
import com.wallet.account_ms.service.BalanceServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@RestController
@RequestMapping("balance")
public class BalanceController {

    @Autowired
    BalanceServiceInterface balanceServiceInterface;
    
    // @PreAuthorize(value = )
    @GetMapping("{id}")
    public ResponseEntity<Double> getBalance(@PathVariable UUID id) {

        BalanceDto response = balanceServiceInterface.getBalance(id).getBody();
        return new ResponseEntity<Double>(response.getValue(), HttpStatus.OK);
    }
    

}
