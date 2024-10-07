package com.wallet.account_ms.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.account_ms.domain.Account;
import com.wallet.account_ms.dto.AccountDto;
import com.wallet.account_ms.service.AccountServiceInterface;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountServiceInterface service;

    @PostMapping()
    public ResponseEntity<UUID> createAccount(@RequestBody AccountDto dto) {
        log.info("ACCOUNT REQUEST RECEIVED: {}", dto.getUsername());
        try {
            this.validateData(dto);
        } catch (BadRequestException e) {
            return new ResponseEntity<UUID>(HttpStatus.BAD_REQUEST);
        }

        Account created;
        try {
            created = service.create(dto);
        } catch (BadRequestException e) {
            return new ResponseEntity<UUID>(HttpStatus.CONFLICT);
        } catch (NotFoundException e) {
            return new ResponseEntity<UUID>(HttpStatus.FAILED_DEPENDENCY);
        }

        
        // criar balance

        log.info("ACCOUNT CREATED: {}", created.getId());
        return new ResponseEntity<UUID>(created.getId(), HttpStatus.CREATED);
    }

    private void validateData(AccountDto dto) throws BadRequestException {
        if (dto.getUsername() == null ||
                dto.getPassword() == null) {
            throw new BadRequestException();
        }
    }

}