package com.farmer.farmermanagement.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password; // Ensure this is hashed before saving

    @Column
    private LocalDate dateOfBirth; // Prefer LocalDate over String

    @Column
    private String gender;

    @Column
    private String country;

    @Column
    private String state;

    @Column
    private String pinCode;

    @Column
    private String timeZone;
}
