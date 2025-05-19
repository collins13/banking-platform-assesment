package com.banking.platform.customer.repositories;

import com.banking.platform.customer.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(CONCAT(c.firstName, ' ', c.lastName, ' ', COALESCE(c.otherName, ''))) " +
            "LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> searchByName(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Customer> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
