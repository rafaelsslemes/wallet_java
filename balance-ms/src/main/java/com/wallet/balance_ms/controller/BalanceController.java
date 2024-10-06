package com.wallet.balance_ms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.balance_ms.dto.UpdateBalanceDto;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("balance")
@Slf4j
public class BalanceController {

    // @Autowired
    // private BalanceService service;

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBalance (@RequestBody UpdateBalanceDto dto, @PathVariable UUID id){
        System.out.println(dto.toString());
        return new ResponseEntity<String>("FAIL", HttpStatus.NOT_FOUND);
    }
}