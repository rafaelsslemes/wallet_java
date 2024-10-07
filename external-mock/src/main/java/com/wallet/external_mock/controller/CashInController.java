package com.wallet.external_mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.external_mock.infra.dto.GenerateDto;
import com.wallet.external_mock.service.GenerateService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("cashin")
@Slf4j
public class CashInController {

    @Autowired
    private GenerateService service;

    @PostMapping
    public void generate (@RequestBody GenerateDto dto){
        for (int i = 0; i < dto.getNumberOfEvents(); i++) {
            service.sendCashIn(dto.getAccountId());
        }
    }
}