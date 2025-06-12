package com.farmer.farmermanagement.entity;

import com.farmer.farmermanagement.enums.CropType;
import com.farmer.farmermanagement.enums.IrrigationSource;
import com.farmer.farmermanagement.enums.SoilTest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LandDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Survey number is required")
    @Column(nullable = false, unique = true)
    private String surveyNumber;

    @Positive(message = "Land size must be greater than zero")
    @Column(nullable = false)
    private double landSize;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Crop type is required")
    @Column(nullable = false)
    private CropType cropType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Soil test is required")
    @Column(nullable = false)
    private SoilTest soilTest;

    @NotBlank(message = "Soil test certificate is required")
    @Column(nullable = false)
    private String soilTestCertificate;

    @NotBlank(message = "GeoTag (Address) is required")
    @Column(nullable = false)
    private String geoTag;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Irrigation source is required")
    @Column(nullable = false)
    private IrrigationSource irrigationSource;

    private String borewellDischarge;
    private String borewellLocation;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "farmer_id", nullable = false, unique = true)
    @JsonIgnore
    private Farmer farmer;
}
