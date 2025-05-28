package com.farmer.farmermanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String iso2; // ISO2 code

    @Column(nullable = true)
    private String dialCode; // Dial code

    @Column(nullable = false)
    private boolean active; // Active status
}
