package com.banking.platform.customer.services;

import com.banking.platform.customer.entities.Customer;
import com.banking.platform.customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomer(UUID id) {
        return customerRepository.findById(id);
    }

    public Customer updateCustomer(UUID id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(customerDetails.getFirstName());
                    customer.setLastName(customerDetails.getLastName());
                    customer.setOtherName(customerDetails.getOtherName());
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }

    public Page<Customer> searchCustomers(String name, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        if (name != null) {
            return customerRepository.searchByName(name, pageable);
        } else if (startDate != null && endDate != null) {
            return customerRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        } else {
            return customerRepository.findAll(pageable);
        }
    }
}
