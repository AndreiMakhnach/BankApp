package com.example.BankApp.repository;

import com.example.BankApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"emails", "phones", "account"})
    Optional<User> findById(Long id);

    @Query("SELECT DISTINCT u FROM User u JOIN u.emails e WHERE e.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT u FROM User u JOIN u.phones p WHERE p.phone = :phone")
    Optional<User> findByPhone(@Param("phone") String phone);

    @Query("SELECT u FROM User u " +
            "WHERE (:name IS NULL OR u.name LIKE CONCAT(:name, '%')) " +
            "AND (:dateOfBirth IS NULL OR u.dateOfBirth > :dateOfBirth) " +
            "AND (:email IS NULL OR EXISTS (SELECT 1 FROM EmailData ed WHERE ed.user = u AND ed.email = :email)) " +
            "AND (:phone IS NULL OR EXISTS (SELECT 1 FROM PhoneData pd WHERE pd.user = u AND pd.phone = :phone))")
    Page<User> findAllWithFilters(
            @Param("name") String name,
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("email") String email,
            @Param("phone") String phone,
            Pageable pageable);
}
