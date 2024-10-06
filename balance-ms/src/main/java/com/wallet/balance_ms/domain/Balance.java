package com.wallet.balance_ms.domain;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    @Id
    @Column(name = "id")
    private UUID accountId;

    @Column(name = "value")
    private double value = 0;

    @Column(name = "lastUpdate")
    private Date lastUpdate = new Date();
    
    @Version
    private int version; // Optimistic locking version field
}
