package com.wallet.external_mock.infra.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class GenerateDto {
    private UUID accountId;
    private int numberOfEvents;
}
