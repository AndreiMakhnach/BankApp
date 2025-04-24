package com.example.BankApp.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserUpdateDto {
    private List<EmailUpdateDto> emails;
    private List<PhoneUpdateDto> phones;
}
