package com.example.BankApp.service;

import com.example.BankApp.dto.UserDto;
import com.example.BankApp.dto.UserSearchRequest;
import com.example.BankApp.dto.UserUpdateDto;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDto findById(Long id);
    Page<UserDto> searchUsers(UserSearchRequest request);
    UserDto updateUser(Long userId, UserUpdateDto updateDTO);
}
