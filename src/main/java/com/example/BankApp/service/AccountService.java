package com.example.BankApp.service;

import com.example.BankApp.dto.TransferRequest;

public interface AccountService {
    void transferMoney(Long fromUserId, TransferRequest request);
    void updateAllBalances();
}
