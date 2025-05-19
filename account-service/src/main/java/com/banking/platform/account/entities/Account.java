package com.banking.platform.account.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "IBAN is mandatory")
    @Column(nullable = false, unique = true)
    private String iban;

    @NotBlank(message = "BIC/SWIFT is mandatory")
    @Column(nullable = false)
    private String bicSwift;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;
}
