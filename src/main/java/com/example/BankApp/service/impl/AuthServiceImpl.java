package com.example.BankApp.service.impl;

import com.example.BankApp.dto.AuthRequest;
import com.example.BankApp.dto.AuthResponse;
import com.example.BankApp.exception.AuthenticationException;
import com.example.BankApp.model.User;
import com.example.BankApp.repository.UserRepository;
import com.example.BankApp.security.JwtTokenProvider;
import com.example.BankApp.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        User user = findUserByCredentials(request);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Ошибка аутентификации: неверные учетные данные");
            throw new AuthenticationException("Неверные учетные данные");
        }

        String token = tokenProvider.createToken(user.getId());
        log.info("Пользователь успешно аутентифицирован: {}", user.getId());
        return new AuthResponse(token);
    }

    private User findUserByCredentials(AuthRequest request) {
        Optional<User> user = Optional.empty();

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user = userRepository.findByEmail(request.getEmail());
        }

        if (user.isEmpty() && request.getPhone() != null && !request.getPhone().isEmpty()) {
            user = userRepository.findByPhone(request.getPhone());
        }

        return user.orElse(null);
    }
}
