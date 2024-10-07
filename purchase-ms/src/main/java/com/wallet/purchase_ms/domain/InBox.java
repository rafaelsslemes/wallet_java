package com.wallet.purchase_ms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class InBox {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INBOX_SEQ")
    private long id;

    @Column(name = "payload", columnDefinition = "json")
    private String payload;

    @Column(name= "processed")
    private boolean processed = false;

}
