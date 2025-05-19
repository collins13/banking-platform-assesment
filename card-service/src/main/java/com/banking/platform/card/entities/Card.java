package com.banking.platform.card.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String alias;

    @Column(name = "account_id", nullable = false, columnDefinition = "UUID")
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType type;

    @NotBlank
    @Column(nullable = false)
    private String pan;

    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "\\d{3}")
    @Column(nullable = false)
    private String cvv;
}




