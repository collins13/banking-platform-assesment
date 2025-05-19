package com.banking.platform.account.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String otherName;
}
