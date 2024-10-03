package com.wallet.external_mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.external_mock.service.GenerateService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("generate")
@Slf4j
public class GenerateController {

    @Autowired
    private GenerateService service;

    @PostMapping
    public void generate (@RequestBody int numberOfEvents){
        for (int i = 0; i < numberOfEvents; i++) {
            service.sendCashIn();
        }
    }
}