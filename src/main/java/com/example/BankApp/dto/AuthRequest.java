package com.example.BankApp.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class AuthRequest {
    private String email;
    private String phone;

    @NotBlank(message = "Пароль обязателен")
    private String password;
}
