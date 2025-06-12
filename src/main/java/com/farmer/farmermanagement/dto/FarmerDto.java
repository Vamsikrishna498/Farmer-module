package com.farmer.farmermanagement.dto;

import com.farmer.farmermanagement.enums.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FarmerDTO {

    private Long id;

    @NotBlank(message = "Photo is required")
    private String photo;

    @NotBlank(message = "Salutation is required")
    private String salutation;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @Pattern(regexp = "\\d{10}", message = "Contact number must be exactly 10 digits")
    private String phoneNumber;

    private String relationshipType;

    @NotBlank(message = "Father name is required")
    private String fatherName;

    @NotBlank(message = "Alternative number is required")
    @Pattern(regexp = "\\d{10}", message = "Alternative number must be exactly 10 digits")
    private String alternativeNumber;

    @NotBlank(message = "Alternative no. type is required")
    private String alternativeNoType;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "Block is required")
    private String block;

    @NotBlank(message = "Village is required")
    private String village;

    @NotBlank(message = "Zipcode is required")
    @Pattern(regexp = "\\d{5,6}", message = "Zipcode must be 5 or 6 digits")
    private String zipcode;

    @NotNull(message = "Education is required")
    private Education education;

    @Min(value = 0, message = "Farming experience cannot be negative")
    private int farmingExperience;

    @DecimalMin(value = "0.0", inclusive = true, message = "Net income cannot be negative")
    private double netIncome;

    @NotNull(message = "Document type is required")
    private Document document;

    @NotBlank(message = "Document path is required")
    private String documentPath;

    @NotBlank(message = "Farmer type is required")
    private String farmerType;

    @NotNull(message = "Portal role is required")
    private PortalRole portalRole;

    @NotNull(message = "Portal access is required")
    private PortalAccess portalAccess;

    private AddressDto address;

    private List<BankDetailsDTO> bankDetails;
    private List<LandDetailsDTO> landDetails;
    private List<CropDTO> crops;

    // Custom setters to enforce null checks
    public void setGender(Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null");
        }
        this.gender = gender;
    }

    public void setEducation(Education education) {
        if (education == null) {
            throw new IllegalArgumentException("Education cannot be null");
        }
        this.education = education;
    }

    public void setDocument(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document type cannot be null");
        }
        this.document = document;
    }

    public void setPortalRole(PortalRole portalRole) {
        if (portalRole == null) {
            throw new IllegalArgumentException("Portal role cannot be null");
        }
        this.portalRole = portalRole;
    }

    public void setPortalAccess(PortalAccess portalAccess) {
        if (portalAccess == null) {
            throw new IllegalArgumentException("Portal access cannot be null");
        }
        this.portalAccess = portalAccess;
    }
}
