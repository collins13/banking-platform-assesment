package com.banking.platform.card.services;

import com.banking.platform.card.dto.AccountDto;
import com.banking.platform.card.entities.Card;
import com.banking.platform.card.entities.CardType;
import com.banking.platform.card.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final RestTemplate restTemplate;
    private static final String ACCOUNT_SERVICE_URL = "http://account-service:8082/api/accounts";


    public Card createCard(Card card) {
        // Verify account exists
        ResponseEntity<AccountDto> response = restTemplate.getForEntity(
                ACCOUNT_SERVICE_URL + "/" + card.getAccountId(),
                AccountDto.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Account not found with id: " + card.getAccountId());
        }

        // Validate max 2 cards per account (1 of each type)
        long existingCards = cardRepository.countByAccountIdAndType(card.getAccountId(), card.getType());
        if (existingCards >= 1) {
            throw new RuntimeException("Account already has a " + card.getType() + " card");
        }

        return cardRepository.save(card);
    }

    public Optional<Card> getCard(UUID id, boolean showSensitive) {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isPresent() && !showSensitive) {
            Card c = card.get();
            c.setPan(maskPan(c.getPan()));
            c.setCvv("***");
        }
        return card;
    }

    public Card updateCard(UUID id, Card cardDetails) {
        return cardRepository.findById(id)
                .map(card -> {
                    // Only alias is editable
                    card.setAlias(cardDetails.getAlias());
                    return cardRepository.save(card);
                })
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));
    }

    public void deleteCard(UUID id) {
        cardRepository.deleteById(id);
    }

    public Page<Card> searchCards(String alias, CardType type, String pan, UUID accountId, boolean showSensitive, Pageable pageable) {
        Page<Card> cards;

        if (alias != null) {
            cards = cardRepository.findByAliasContaining(alias, pageable);
        } else if (type != null) {
            cards = cardRepository.findByType(type, pageable);
        } else if (pan != null) {
            cards = cardRepository.findByPanContaining(pan, pageable);
        } else if (accountId != null) {
            cards = cardRepository.findByAccountId(accountId, pageable);
        } else {
            cards = cardRepository.findAll(pageable);
        }

        if (!showSensitive) {
            cards = cards.map(card -> {
                card.setPan(maskPan(card.getPan()));
                card.setCvv("***");
                return card;
            });
        }

        return cards;
    }

    public AccountDto getAccountDetails(UUID accountId) {
        ResponseEntity<AccountDto> response = restTemplate.getForEntity(
                ACCOUNT_SERVICE_URL + "/" + accountId,
                AccountDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("Failed to fetch account details");
    }

    private String maskPan(String pan) {
        if (pan == null || pan.length() < 8) {
            return pan;
        }
        return pan.substring(0, 4) + "********" + pan.substring(pan.length() - 4);
    }
}
