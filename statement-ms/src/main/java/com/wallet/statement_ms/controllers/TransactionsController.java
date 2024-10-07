package com.wallet.statement_ms.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.statement_ms.domain.Transaction;
import com.wallet.statement_ms.services.TransactionServiceInterface;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("transactions")
public class TransactionsController {

    @Autowired
    private TransactionServiceInterface service;
    
    @GetMapping("/{id}")
    public ResponseEntity<List<Transaction>> getMethodName(@PathVariable UUID id, @RequestParam int page) {

        try {
            List<Transaction> transactions = service.listPaginated(id, page);

            return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_FOUND);

        }
    }
    
}
