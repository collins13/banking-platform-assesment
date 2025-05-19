package com.banking.platform.customer.dto;

import lombok.Data;

import java.rmi.server.UID;

@Data
public class CustomerDto {
    private UID id;
    private String firstName;
    private String lastName;
    private String otherName;
}
