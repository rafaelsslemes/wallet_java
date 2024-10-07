package com.wallet.account_ms.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @Column(name = "id")
    private UUID id;

    @OneToOne
    private User user;
    
}
