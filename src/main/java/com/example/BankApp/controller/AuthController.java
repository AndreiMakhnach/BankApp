package com.example.BankApp.controller;

import com.example.BankApp.dto.AuthRequest;
import com.example.BankApp.dto.AuthResponse;
import com.example.BankApp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Аутентификация пользователя и получение JWT токена")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("Запрос на аутентификацию пользователя: {}", request.getEmail() != null ? request.getEmail() : request.getPhone());
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
