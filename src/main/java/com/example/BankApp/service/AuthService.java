package com.example.BankApp.service;


import com.example.BankApp.dto.AuthRequest;
import com.example.BankApp.dto.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
}
