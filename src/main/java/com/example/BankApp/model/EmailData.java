package com.example.BankApp.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "EMAIL_DATA")
public class EmailData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "EMAIL", length = 200, unique = true)
    private String email;
}
