package com.banking.platform.card.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CardDto {
    private UUID id;
    private String alias;
    private Long accountId;
    private String type;
    private String pan;
    private String cvv;
}
