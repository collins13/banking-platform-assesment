package com.banking.platform.customer;

import com.banking.platform.customer.entities.Customer;
import com.banking.platform.customer.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(TestConfig.class) // Optional: if you need any test-specific config
public class CustomerServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testCreateCustomer() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        assertNotNull(savedCustomer.getId());
        assertEquals("John", savedCustomer.getFirstName());
    }

    // Update other tests to work with UUID
    @Test
    public void testFindCustomerById() {
        Customer customer = Customer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();
        customer = entityManager.persistAndFlush(customer);

        Optional<Customer> foundCustomer = customerRepository.findById(customer.getId());
        assertTrue(foundCustomer.isPresent());
        assertEquals("Jane", foundCustomer.get().getFirstName());
    }
}