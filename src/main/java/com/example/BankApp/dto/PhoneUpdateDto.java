package com.example.BankApp.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PhoneUpdateDto {
    private Long id;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^7\\d{10}$", message = "Телефон должен быть в формате 79207654321")
    private String phone;

    private boolean toDelete;
}
