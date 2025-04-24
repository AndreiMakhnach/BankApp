package com.example.BankApp.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BankApp.dto.TransferRequest;
import com.example.BankApp.exception.InsufficientBalanceException;
import com.example.BankApp.exception.ResourceNotFoundException;
import com.example.BankApp.model.Account;
import com.example.BankApp.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(new BigDecimal("1000.00"));
        fromAccount.setInitialBalance(new BigDecimal("1000.00"));

        toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(new BigDecimal("500.00"));
        toAccount.setInitialBalance(new BigDecimal("500.00"));
    }

    @Test
    public void testTransferMoney_Success() {
        TransferRequest request = new TransferRequest();
        request.setRecipientId(2L);
        request.setAmount(new BigDecimal("200.00"));

        when(accountRepository.findByUserIdWithLock(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdWithLock(2L)).thenReturn(Optional.of(toAccount));

        accountService.transferMoney(1L, request);

        assertEquals(new BigDecimal("800.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), toAccount.getBalance());

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }

    @Test
    public void testTransferMoney_InsufficientBalance() {
        TransferRequest request = new TransferRequest();
        request.setRecipientId(2L);
        request.setAmount(new BigDecimal("2000.00"));

        when(accountRepository.findByUserIdWithLock(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdWithLock(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.transferMoney(1L, request);
        });
    }

    @Test
    public void testTransferMoney_AccountNotFound() {
        TransferRequest request = new TransferRequest();
        request.setRecipientId(2L);
        request.setAmount(new BigDecimal("100.00"));

        when(accountRepository.findByUserIdWithLock(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.transferMoney(1L, request);
        });
    }
}
