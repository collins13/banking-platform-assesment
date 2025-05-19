package com.banking.platform.account.services;

import com.banking.platform.account.dto.CustomerDto;
import com.banking.platform.account.entities.Account;
import com.banking.platform.account.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;
    private static final String CUSTOMER_SERVICE_URL = "http://customer-service:8081/api/customers";


    public Account createAccount(Account account) throws ServiceUnavailableException {
        validateCustomerExists(account.getCustomerId());
        return accountRepository.save(account);
    }

    private void validateCustomerExists(UUID customerId) throws ServiceUnavailableException {
        try {
            ResponseEntity<CustomerDto> response = restTemplate.getForEntity(
                    "http://customer-service/api/customers/{id}",
                    CustomerDto.class,
                    customerId
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new CustomerNotFoundException("Customer not found with id: " + customerId);
            }
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CustomerNotFoundException("Customer not found with id: " + customerId);
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("Customer service is currently unavailable");
        }
    }

    public Optional<Account> getAccount(UUID id) {
        return accountRepository.findById(id);
    }

    public Account updateAccount(UUID id, Account accountDetails) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setIban(accountDetails.getIban());
                    account.setBicSwift(accountDetails.getBicSwift());
                    return accountRepository.save(account);
                })
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
    }

    public Page<Account> searchAccounts(String iban, String bicSwift, UUID customerId, Pageable pageable) {
        if (iban != null) {
            return accountRepository.findByIbanContaining(iban, pageable);
        } else if (bicSwift != null) {
            return accountRepository.findByBicSwiftContaining(bicSwift, pageable);
        } else if (customerId != null) {
            return accountRepository.findByCustomerId(customerId, pageable);
        } else {
            return accountRepository.findAll(pageable);
        }
    }

    public CustomerDto getCustomerDetails(UUID customerId) {
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity(
                CUSTOMER_SERVICE_URL + "/" + customerId,
                CustomerDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("Failed to fetch customer details");
    }
}
