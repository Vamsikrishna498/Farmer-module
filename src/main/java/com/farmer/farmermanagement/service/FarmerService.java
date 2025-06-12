package com.farmer.farmermanagement.service;

import com.farmer.farmermanagement.dto.BankDetailsDto;
import com.farmer.farmermanagement.dto.FarmerDto;
import com.farmer.farmermanagement.dto.LandDetailsDto;
import com.farmer.farmermanagement.entity.BankDetails;
import com.farmer.farmermanagement.entity.Crop;
import com.farmer.farmermanagement.entity.Farmer;
import com.farmer.farmermanagement.entity.LandDetails;
import com.farmer.farmermanagement.exception.FarmerNotFoundException;
import com.farmer.farmermanagement.mapper.AddressMapper;
import com.farmer.farmermanagement.mapper.CropMapper;
import com.farmer.farmermanagement.mapper.FarmerMapper;
import com.farmer.farmermanagement.repository.BankDetailsRepository;
import com.farmer.farmermanagement.repository.CropRepository;
import com.farmer.farmermanagement.repository.FarmerRepository;
import com.farmer.farmermanagement.repository.LandDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final LandDetailsRepository landDetailsRepository;
    private final CropRepository cropRepository;
    private final FarmerMapper farmerMapper;
    private final CropMapper cropMapper;
    private final AddressMapper addressMapper;

    public FarmerDto registerFarmer(@Valid FarmerDto farmerDto) {
        Farmer farmer = farmerMapper.toEntity(farmerDto);

        if (farmer.getCrops() != null) {
            farmer.getCrops().forEach(crop -> crop.setFarmer(farmer));
        }

        if (farmer.getBankDetails() != null) {
            farmer.getBankDetails().setFarmer(farmer);
        }

        if (farmer.getLandDetails() != null) {
            farmer.getLandDetails().setFarmer(farmer);
        }

        Farmer savedFarmer = farmerRepository.save(farmer);
        return farmerMapper.toDto(savedFarmer);
    }

    public FarmerDto updateFarmer(Long id, @Valid FarmerDto farmerDto) {
        Farmer existing = farmerRepository.findById(id)
                .orElseThrow(() -> new FarmerNotFoundException("Farmer not found with ID: " + id));

        // Update core fields
        existing.setFirstName(farmerDto.getFirstName());
        existing.setMiddleName(farmerDto.getMiddleName());
        existing.setLastName(farmerDto.getLastName());
        existing.setPhoneNumber(farmerDto.getPhoneNumber());
        existing.setDateOfBirth(farmerDto.getDateOfBirth());
        existing.setGender(farmerDto.getGender());
        existing.setEducation(farmerDto.getEducation());
        existing.setDocument(farmerDto.getDocument());
        existing.setPortalAccess(farmerDto.getPortalAccess());
        existing.setPortalRole(farmerDto.getPortalRole());
        existing.setFarmerType(farmerDto.getFarmerType());

        existing.setAddress(addressMapper.toEntity(farmerDto.getAddress()));

        BankDetailsDto bankDetailsDto = farmerDto.getBankDetails();
        if (bankDetailsDto != null) {
            BankDetails bankDetails = existing.getBankDetails();
            bankDetails.setAccountNumber(bankDetailsDto.getAccountNumber());
            bankDetails.setBankName(bankDetailsDto.getBankName().toString());
            bankDetails.setIfscCode(bankDetailsDto.getIfscCode());
            bankDetails.setFarmer(existing);
        }

        LandDetailsDto landDetailsDto = farmerDto.getLandDetails();
        if (landDetailsDto != null) {
            LandDetails landDetails = existing.getLandDetails();
            landDetails.setBorewellDischarge(landDetailsDto.getBorewellDischarge());
            landDetails.setBorewellLocation(landDetailsDto.getBorewellLocation());
            landDetails.setCropType(landDetailsDto.getCropType());
            landDetails.setGeoTag(landDetailsDto.getGeoTag());
            landDetails.setIrrigationSource(landDetailsDto.getIrrigationSource());
            landDetails.setLandSize(landDetailsDto.getLandSize());
            landDetails.setSoilTest(landDetailsDto.getSoilTest());
            landDetails.setSoilTestCertificate(landDetailsDto.getSoilTestCertificate());
            landDetails.setLatitude(landDetailsDto.getLatitude());
            landDetails.setLongitude(landDetailsDto.getLongitude());
            landDetails.setFarmer(existing);
        }

        // Update crops
        if (farmerDto.getCrops() != null) {
            existing.getCrops().clear();
            existing.getCrops().addAll(farmerDto.getCrops().stream().map(cropDto -> {
                Crop crop = cropMapper.toEntity(cropDto);
                crop.setFarmer(existing);
                return crop;
            }).toList());
        }

        Farmer updated = farmerRepository.save(existing);
        return farmerMapper.toDto(updated);
    }

    public FarmerDto getFarmerById(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new FarmerNotFoundException("Farmer not found with ID: " + id));
        return farmerMapper.toDto(farmer);
    }

    public List<FarmerDto> getAllFarmers() {
        return farmerRepository.findAll().stream()
                .map(farmerMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteFarmer(Long id) {
        if (!farmerRepository.existsById(id)) {
            throw new EntityNotFoundException("Farmer not found with ID: " + id);
        }
        farmerRepository.deleteById(id);
    }
}
