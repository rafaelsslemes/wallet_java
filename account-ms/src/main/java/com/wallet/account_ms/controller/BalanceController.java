package com.wallet.account_ms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("balance")
public class BalanceController {
    
    // @PreAuthorize(value = )
    @GetMapping()
    public String getMethodName(@RequestParam String param) {
        return new String("Teste");
    }
    

}
