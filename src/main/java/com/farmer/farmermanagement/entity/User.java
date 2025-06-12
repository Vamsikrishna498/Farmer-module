package com.farmer.farmermanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
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
    private String password; // Ensure this is hashed when storing

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
