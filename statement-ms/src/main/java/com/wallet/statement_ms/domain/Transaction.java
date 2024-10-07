package com.wallet.statement_ms.domain;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
public class Transaction {
    
    @Id
    @Column(name = "id")
    private UUID eventId; // Controle de idempotência
    
    @Column(name= "sourceId")
    private UUID sourceId; // Qual a instituição que enviou
    
    @Column(name= "payerId")
    private UUID payerId; // Conta de quem pagou
    
    @Column(name= "receiverId")
    private UUID receiverId; // Conta de quem recebeu

    @Column(name= "details")
    private String details;
    
    @Column(name= "value")
    private double value;

    @Column(name= "date")
    private Date date;
}