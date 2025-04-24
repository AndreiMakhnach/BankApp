package com.example.BankApp.service.impl;

import com.example.BankApp.dto.AccountDto;
import com.example.BankApp.dto.EmailUpdateDto;
import com.example.BankApp.dto.PhoneUpdateDto;
import com.example.BankApp.dto.UserDto;
import com.example.BankApp.dto.UserSearchRequest;
import com.example.BankApp.dto.UserUpdateDto;
import com.example.BankApp.exception.ResourceNotFoundException;
import com.example.BankApp.model.EmailData;
import com.example.BankApp.model.PhoneData;
import com.example.BankApp.model.User;
import com.example.BankApp.repository.EmailDataRepository;
import com.example.BankApp.repository.PhoneDataRepository;
import com.example.BankApp.repository.UserRepository;
import com.example.BankApp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserDto findById(Long id) {
        log.info("Поиск пользователя по ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден: " + id));
        return mapToDTO(user);
    }

    @Override
    @Cacheable(value = "userSearches", key = "#request.toString()")
    public Page<UserDto> searchUsers(UserSearchRequest request) {
        log.info("Поиск пользователей с фильтрами: {}", request);
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<User> users = userRepository.findAllWithFilters(
                request.getName(),
                request.getDateOfBirth(),
                request.getEmail(),
                request.getPhone(),
                pageable
        );

        return users.map(this::mapToDTO);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"users", "userSearches"}, allEntries = true)
    public UserDto updateUser(Long userId, UserUpdateDto updateDTO) {
        log.info("Обновление пользователя ID: {} с данными: {}", userId, updateDTO);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден: " + userId));

        // Обновление email-адресов
        if (updateDTO.getEmails() != null && !updateDTO.getEmails().isEmpty()) {
            updateEmails(user, updateDTO.getEmails());
        }

        // Обновление телефонов
        if (updateDTO.getPhones() != null && !updateDTO.getPhones().isEmpty()) {
            updatePhones(user, updateDTO.getPhones());
        }

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    private void updateEmails(User user, List<EmailUpdateDto> emailUpdates) {
        List<EmailData> currentEmails = new ArrayList<>(user.getEmails());

        // Обработка удалений
        emailUpdates.stream()
                .filter(EmailUpdateDto::isToDelete)
                .filter(dto -> dto.getId() != null)
                .forEach(dto -> {
                    if (currentEmails.size() <= 1) {
                        throw new ValidationException("Нельзя удалить последний email");
                    }

                    currentEmails.removeIf(email -> Objects.equals(email.getId(), dto.getId()));
                });

        // Обработка обновлений и добавлений
        emailUpdates.stream()
                .filter(dto -> !dto.isToDelete())
                .forEach(dto -> {
                    // Проверка существования email для другого пользователя
                    if (emailDataRepository.existsByEmail(dto.getEmail())) {
                        EmailData existingEmail = emailDataRepository.findByEmail(dto.getEmail())
                                .orElseThrow();

                        if (!Objects.equals(existingEmail.getUser().getId(), user.getId())) {
                            throw new ValidationException("Email " + dto.getEmail() + " уже используется");
                        }
                    }

                    if (dto.getId() != null) {
                        // Обновление существующего email
                        currentEmails.stream()
                                .filter(email -> Objects.equals(email.getId(), dto.getId()))
                                .findFirst()
                                .ifPresent(email -> email.setEmail(dto.getEmail()));
                    } else {
                        // Добавление нового email
                        EmailData newEmail = new EmailData();
                        newEmail.setEmail(dto.getEmail());
                        newEmail.setUser(user);
                        currentEmails.add(newEmail);
                    }
                });

        user.getEmails().clear();
        user.getEmails().addAll(currentEmails);
    }

    private void updatePhones(User user, List<PhoneUpdateDto> phoneUpdates) {
        List<PhoneData> currentPhones = new ArrayList<>(user.getPhones());

        // Обработка удалений
        phoneUpdates.stream()
                .filter(PhoneUpdateDto::isToDelete)
                .filter(dto -> dto.getId() != null)
                .forEach(dto -> {
                    if (currentPhones.size() <= 1) {
                        throw new ValidationException("Нельзя удалить последний телефон");
                    }

                    currentPhones.removeIf(phone -> Objects.equals(phone.getId(), dto.getId()));
                });

        // Обработка обновлений и добавлений
        phoneUpdates.stream()
                .filter(dto -> !dto.isToDelete())
                .forEach(dto -> {
                    // Проверка существования телефона для другого пользователя
                    if (phoneDataRepository.existsByPhone(dto.getPhone())) {
                        PhoneData existingPhone = phoneDataRepository.findByPhone(dto.getPhone())
                                .orElseThrow();

                        if (!Objects.equals(existingPhone.getUser().getId(), user.getId())) {
                            throw new ValidationException("Телефон " + dto.getPhone() + " уже используется");
                        }
                    }

                    if (dto.getId() != null) {
                        // Обновление существующего телефона
                        currentPhones.stream()
                                .filter(phone -> Objects.equals(phone.getId(), dto.getId()))
                                .findFirst()
                                .ifPresent(phone -> phone.setPhone(dto.getPhone()));
                    } else {
                        // Добавление нового телефона
                        PhoneData newPhone = new PhoneData();
                        newPhone.setPhone(dto.getPhone());
                        newPhone.setUser(user);
                        currentPhones.add(newPhone);
                    }
                });

        user.getPhones().clear();
        user.getPhones().addAll(currentPhones);
    }

    private UserDto mapToDTO(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());

        // Маппинг email-адресов
        dto.setEmails(user.getEmails().stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList()));

        // Маппинг телефонов
        dto.setPhones(user.getPhones().stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList()));

        // Маппинг счета
        if (user.getAccount() != null) {
            AccountDto accountDTO = new AccountDto();
            accountDTO.setId(user.getAccount().getId());
            accountDTO.setBalance(user.getAccount().getBalance());
            dto.setAccount(accountDTO);
        }

        return dto;
    }
}
