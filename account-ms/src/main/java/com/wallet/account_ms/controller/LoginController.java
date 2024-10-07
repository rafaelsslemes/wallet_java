package com.wallet.account_ms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("login")
public class LoginController {
    
    @PostMapping()
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return username;
    }
    
}
