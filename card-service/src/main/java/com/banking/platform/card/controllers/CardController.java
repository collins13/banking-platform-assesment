package com.banking.platform.card.controllers;

import com.banking.platform.card.dto.AccountDto;
import com.banking.platform.card.entities.Card;
import com.banking.platform.card.entities.CardType;
import com.banking.platform.card.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody Card card) {
        try {
            Card savedCard = cardService.createCard(card);
            return ResponseEntity.ok(savedCard);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCard(@PathVariable UUID id,
                                        @RequestParam(defaultValue = "false") boolean showSensitive) {
        Optional<Card> card = cardService.getCard(id, showSensitive);
        return card.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable UUID id, @RequestBody Card cardDetails) {
        try {
            Card updatedCard = cardService.updateCard(id, cardDetails);
            return ResponseEntity.ok(updatedCard);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Card>> getAllCards(
            @RequestParam(required = false) String alias,
            @RequestParam(required = false) CardType type,
            @RequestParam(required = false) String pan,
            @RequestParam(required = false) UUID accountId,
            @RequestParam(defaultValue = "false") boolean showSensitive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Card> cards = cardService.searchCards(alias, type, pan, accountId, showSensitive, pageable);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{id}/account")
    public ResponseEntity<AccountDto> getCardAccount(@PathVariable UUID id) {
        Optional<Card> card = cardService.getCard(id, false);
        if (!card.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            AccountDto account = cardService.getAccountDetails(card.get().getAccountId());
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
