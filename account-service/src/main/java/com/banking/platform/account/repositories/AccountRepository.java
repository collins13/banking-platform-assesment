package com.banking.platform.account.repositories;

import com.banking.platform.account.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Page<Account> findByIbanContaining(String iban, Pageable pageable);
    Page<Account> findByBicSwiftContaining(String bicSwift, Pageable pageable);
    Page<Account> findByCustomerId(UUID customerId, Pageable pageable);
}
