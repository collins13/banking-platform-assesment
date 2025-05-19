package com.banking.platform.account;

import com.banking.platform.account.entities.Account;
import com.banking.platform.account.repositories.AccountRepository;
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
class AccountServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private final UUID customerId1 = UUID.randomUUID();
    private final UUID customerId2 = UUID.randomUUID();

    @Test
    void testCreateAccount() {
        Account account = Account.builder()
                .iban("GB29NWBK60161331926819")
                .bicSwift("BARCGB22")
                .customerId(customerId1)
                .build();

        Account savedAccount = accountRepository.save(account);
        assertNotNull(savedAccount.getId());
        assertEquals("GB29NWBK60161331926819", savedAccount.getIban());
    }

    @Test
    void testFindAccountById() {
        Account account = Account.builder()
                .iban("GB29NWBK60161331926819")
                .bicSwift("BARCGB22")
                .customerId(customerId1)
                .build();
        account = entityManager.persistAndFlush(account);

        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        assertTrue(foundAccount.isPresent());
        assertEquals("BARCGB22", foundAccount.get().getBicSwift());
    }

    @Test
    void testFindByIban() {
        Account account1 = Account.builder()
                .iban("GB29NWBK60161331926819")
                .bicSwift("BARCGB22")
                .customerId(customerId1)
                .build();

        Account account2 = Account.builder()
                .iban("GB29NWBK60161331926820")
                .bicSwift("BARCGB22")
                .customerId(customerId2)
                .build();

        entityManager.persist(account1);
        entityManager.persist(account2);
        entityManager.flush();

        var result = accountRepository.findByIbanContaining("60161331926819", Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        assertEquals("GB29NWBK60161331926819", result.getContent().get(0).getIban());
    }

    @Test
    void testFindByBicSwift() {
        Account account1 = Account.builder()
                .iban("GB29NWBK60161331926819")
                .bicSwift("BARCGB22")
                .customerId(customerId1)
                .build();

        Account account2 = Account.builder()
                .iban("GB29NWBK60161331926820")
                .bicSwift("BARCGB23")
                .customerId(customerId2)
                .build();

        entityManager.persist(account1);
        entityManager.persist(account2);
        entityManager.flush();

        var result = accountRepository.findByBicSwiftContaining("BARCGB22", Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        assertEquals("BARCGB22", result.getContent().get(0).getBicSwift());
    }

    @Test
    void testFindByCustomerId() {
        Account account1 = Account.builder()
                .iban("GB29NWBK60161331926819")
                .bicSwift("BARCGB22")
                .customerId(customerId1)
                .build();

        Account account2 = Account.builder()
                .iban("GB29NWBK60161331926820")
                .bicSwift("BARCGB22")
                .customerId(customerId2)
                .build();

        entityManager.persist(account1);
        entityManager.persist(account2);
        entityManager.flush();

        var result = accountRepository.findByCustomerId(customerId1, Pageable.unpaged());
        assertEquals(1, result.getTotalElements());
        assertEquals(customerId1, result.getContent().get(0).getCustomerId());
    }
}