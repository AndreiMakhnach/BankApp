package com.example.BankApp.service.impl;

import com.example.BankApp.dto.TransferRequest;
import com.example.BankApp.exception.InsufficientBalanceException;
import com.example.BankApp.exception.ResourceNotFoundException;
import com.example.BankApp.model.Account;
import com.example.BankApp.repository.AccountRepository;
import com.example.BankApp.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    @CacheEvict(value = {"users", "userSearches"}, allEntries = true)
    public void transferMoney(Long fromUserId, TransferRequest request) {
        log.info("Инициирован перевод от пользователя {} к {} на сумму {}",
                fromUserId, request.getRecipientId(), request.getAmount());

        Account fromAccount = accountRepository.findByUserIdWithLock(fromUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Счет отправителя не найден"));

        Account toAccount = accountRepository.findByUserIdWithLock(request.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Счет получателя не найден"));

        validateTransfer(fromAccount, toAccount, request.getAmount());

        performTransfer(fromAccount, toAccount, request.getAmount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("Перевод успешно завершен. Новый баланс отправителя: {}, получателя: {}",
                fromAccount.getBalance(), toAccount.getBalance());
    }

    private void validateTransfer(Account from, Account to, BigDecimal amount) {
        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Недостаточно средств на счете");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительной");
        }

        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("Нельзя переводить самому себе");
        }
    }

    private void performTransfer(Account from, Account to, BigDecimal amount) {
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 30000)
    @CacheEvict(value = {"users", "userSearches"}, allEntries = true)
    public void updateAllBalances() {
        log.info("Запуск автоматического обновления балансов");

        List<Account> accounts = accountRepository.findAllAccounts();

        for (Account account : accounts) {
            BigDecimal newBalance = calculateNewBalance(account);

            if (newBalance.compareTo(account.getInitialBalance().multiply(BigDecimal.valueOf(2.07))) <= 0) {
                account.setBalance(newBalance);
                accountRepository.save(account);
                log.info("Обновлен баланс счета {}: {}", account.getId(), newBalance);
            } else {
                log.info("Баланс счета {} достиг лимита в 207%", account.getId());
            }
        }
    }

    private BigDecimal calculateNewBalance(Account account) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal increase = currentBalance.multiply(BigDecimal.valueOf(0.1));

        return currentBalance.add(increase).setScale(2, RoundingMode.HALF_UP);
    }
}
