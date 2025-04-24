package com.example.BankApp.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "PHONE_DATA")
public class PhoneData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "PHONE", length = 13, unique = true)
    private String phone;
}
