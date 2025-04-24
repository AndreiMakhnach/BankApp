package com.example.BankApp.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private List<String> emails;
    private List<String> phones;
    private AccountDto account;
}
