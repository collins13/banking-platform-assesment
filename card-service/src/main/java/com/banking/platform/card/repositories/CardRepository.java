package com.banking.platform.card.repositories;

import com.banking.platform.card.entities.Card;
import com.banking.platform.card.entities.CardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    Page<Card> findByAliasContaining(String alias, Pageable pageable);
    Page<Card> findByType(CardType type, Pageable pageable);
    Page<Card> findByPanContaining(String pan, Pageable pageable);
    Page<Card> findByAccountId(UUID accountId, Pageable pageable);

    long countByAccountIdAndType(UUID accountId, CardType type);
}
