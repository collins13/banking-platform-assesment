package com.banking.platform.card.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountDto {
    private UUID id;
    private String iban;
    private String bicSwift;
    private Long customerId;
}
