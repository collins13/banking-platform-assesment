package com.banking.platform.card;

import com.banking.platform.card.entities.Card;
import com.banking.platform.card.entities.CardType;
import com.banking.platform.card.repositories.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CardServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository cardRepository;

    private final UUID testAccountId1 = UUID.randomUUID();
    private final UUID testAccountId2 = UUID.randomUUID();

    @Test
    void testCreateCard() {
        Card card = Card.builder()
                .alias("My Personal Card")
                .accountId(testAccountId1)
                .type(CardType.PHYSICAL)
                .pan("4111111111111111")
                .cvv("123")
                .build();

        Card savedCard = cardRepository.save(card);
        assertNotNull(savedCard.getId());
        assertEquals("My Personal Card", savedCard.getAlias());
    }

    @Test
    void testFindCardById() {
        Card card = Card.builder()
                .alias("My Personal Card")
                .accountId(testAccountId1)
                .type(CardType.PHYSICAL)
                .pan("4111111111111111")
                .cvv("123")
                .build();
        card = entityManager.persistAndFlush(card);

        Optional<Card> foundCard = cardRepository.findById(card.getId());
        assertTrue(foundCard.isPresent());
        assertEquals(CardType.PHYSICAL, foundCard.get().getType());
    }

    @Test
    void testFindByAlias() {
        Card card1 = Card.builder()
                .alias("My Personal Card")
                .accountId(testAccountId1)
                .type(CardType.PHYSICAL)
                .pan("4111111111111111")
                .cvv("123")
                .build();

        Card card2 = Card.builder()
                .alias("Business Card")
                .accountId(testAccountId2)
                .type(CardType.VIRTUAL)
                .pan("5555555555554444")
                .cvv("456")
                .build();

        entityManager.persist(card1);
        entityManager.persist(card2);
        entityManager.flush();

        var result = cardRepository.findByAliasContaining("Personal", Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        assertEquals("My Personal Card", result.getContent().get(0).getAlias());
    }

    @Test
    void testFindByType() {
        Card card1 = Card.builder()
                .alias("My Personal Card")
                .accountId(testAccountId1)
                .type(CardType.PHYSICAL)
                .pan("4111111111111111")
                .cvv("123")
                .build();

        Card card2 = Card.builder()
                .alias("Business Card")
                .accountId(testAccountId2)
                .type(CardType.VIRTUAL)
                .pan("5555555555554444")
                .cvv("456")
                .build();

        entityManager.persist(card1);
        entityManager.persist(card2);
        entityManager.flush();

        var result = cardRepository.findByType(CardType.VIRTUAL, Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        assertEquals(CardType.VIRTUAL, result.getContent().get(0).getType());
    }

    @Test
    void testFindByPan() {
        Card card1 = Card.builder()
                .alias("My Personal Card")
                .accountId(testAccountId1)
                .type(CardType.PHYSICAL)
                .pan("4111111111111111")
                .cvv("123")
                .build();

        Card card2 = Card.builder()
                .alias("Business Card")
                .accountId(testAccountId2)
                .type(CardType.VIRTUAL)
                .pan("5555555555554444")
                .cvv("456")
                .build();

        entityManager.persist(card1);
        entityManager.persist(card2);
        entityManager.flush();

        var result = cardRepository.findByPanContaining("1111", Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        assertEquals("4111111111111111", result.getContent().get(0).getPan());
    }

    @Test
    void testCountByAccountIdAndType() {
        Card card1 = Card.builder()
                .alias("My Personal Card")
                .accountId(testAccountId1)
                .type(CardType.PHYSICAL)
                .pan("4111111111111111")
                .cvv("123")
                .build();

        Card card2 = Card.builder()
                .alias("Business Card")
                .accountId(testAccountId2)
                .type(CardType.VIRTUAL)
                .pan("5555555555554444")
                .cvv("456")
                .build();

        Card card3 = Card.builder()
                .alias("Extra Virtual")
                .accountId(testAccountId1)
                .type(CardType.VIRTUAL)
                .pan("4000000000000002")
                .cvv("789")
                .build();

        entityManager.persist(card1);
        entityManager.persist(card2);
        entityManager.persist(card3);
        entityManager.flush();

        long physicalCount = cardRepository.countByAccountIdAndType(testAccountId1, CardType.PHYSICAL);
        assertEquals(1, physicalCount);

        long virtualCount = cardRepository.countByAccountIdAndType(testAccountId1, CardType.VIRTUAL);
        assertEquals(1, virtualCount);
    }
}
