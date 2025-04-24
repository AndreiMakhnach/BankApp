package com.example.BankApp.controller;

import com.example.BankApp.dto.UserDto;
import com.example.BankApp.dto.UserSearchRequest;
import com.example.BankApp.dto.UserUpdateDto;
import com.example.BankApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по ID")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Запрос на получение пользователя с ID: {}", id);
        UserDto user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск пользователей с фильтрацией и пагинацией")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<UserDto>> searchUsers(UserSearchRequest request) {
        log.info("Запрос на поиск пользователей с параметрами: {}", request);
        Page<UserDto> users = userService.searchUsers(request);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление данных пользователя")
    @PreAuthorize("isAuthenticated() and #id == principal.id")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto updateDTO) {
        log.info("Запрос на обновление пользователя с ID: {} и данными: {}", id, updateDTO);
        UserDto updatedUser = userService.updateUser(id, updateDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
