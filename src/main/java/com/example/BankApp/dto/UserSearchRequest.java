package com.example.BankApp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserSearchRequest {
    private String name;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private Integer page = 0;
    private Integer size = 10;
}
