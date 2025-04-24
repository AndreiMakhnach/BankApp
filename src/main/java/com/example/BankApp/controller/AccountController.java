package com.example.BankApp.controller;

import com.example.BankApp.dto.TransferRequest;
import com.example.BankApp.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/transfer")
    @Operation(summary = "Перевод денег от одного пользователя к другому")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> transferMoney(@RequestBody TransferRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        log.info("Запрос на перевод денег от пользователя {} к пользователю {} на сумму {}",
                userId, request.getRecipientId(), request.getAmount());

        accountService.transferMoney(userId, request);
        return ResponseEntity.ok().build();
    }
}
