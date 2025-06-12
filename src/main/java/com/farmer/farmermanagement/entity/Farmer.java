package com.farmer.farmermanagement.entity;

import com.farmer.farmermanagement.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Photo is required")
    @Column(nullable = false)
    private String photo;

    @NotBlank(message = "Salutation is required")
    @Column(nullable = false)
    private String salutation;

    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    private String middleName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required")
    @Column(nullable = false)
    private Gender gender;

    @NotBlank(message = "Nationality is required")
    @Column(nullable = false)
    private String nationality;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dob;

    @Pattern(regexp = "\\d{10}", message = "Contact number must be exactly 10 digits")
    @Column(nullable = false, unique = true)
    private String contactNumber;

    private String relationshipType;

    @NotBlank(message = "Father name is required")
    @Column(nullable = false)
    private String fatherName;

    @NotBlank(message = "Alternative number is required")
    @Pattern(regexp = "\\d{10}", message = "Alternative number must be exactly 10 digits")
    @Column(nullable = false)
    private String alternativeNumber;

    @NotBlank(message = "Alternative no. type is required")
    @Column(nullable = false)
    private String alternativeNoType;

    @NotBlank(message = "Country is required")
    @Column(nullable = false)
    private String country;

    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;

    @NotBlank(message = "District is required")
    @Column(nullable = false)
    private String district;

    @NotBlank(message = "Block is required")
    @Column(nullable = false)
    private String block;

    @NotBlank(message = "Village is required")
    @Column(nullable = false)
    private String village;

    @NotBlank(message = "Zipcode is required")
    @Pattern(regexp = "\\d{5,6}", message = "Zipcode must be 5 or 6 digits")
    @Column(nullable = false)
    private String zipcode;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Education is required")
    @Column(nullable = false)
    private Education education;

    @Min(value = 0, message = "Farming experience cannot be negative")
    private int farmingExperience;

    @DecimalMin(value = "0.0", inclusive = true, message = "Net income cannot be negative")
    private double netIncome;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Document is required")
    @Column(nullable = false)
    private Document document;

    @NotBlank(message = "Document path is required")
    @Column(nullable = false)
    private String documentPath;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Portal role is required")
    @Column(nullable = false)
    private PortalRole portalRole;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Portal access is required")
    @Column(nullable = false)
    private PortalAccess portalAccess;

    @NotBlank(message = "Farmer type is required")
    @Column(nullable = false)
    private String farmerType;  // ✅ Added this field for mapper compatibility

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Crop> crops;

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BankDetails> bankDetails;

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LandDetails> landDetails;
}
