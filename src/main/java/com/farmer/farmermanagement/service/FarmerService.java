package com.farmer.farmermanagement.service;

import com.farmer.farmermanagement.dto.*;
import com.farmer.farmermanagement.entity.*;
import com.farmer.farmermanagement.enums.BankName;
import com.farmer.farmermanagement.exception.FarmerNotFoundException;
import com.farmer.farmermanagement.mapper.FarmerMapper;
import com.farmer.farmermanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FarmerService {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private BankDetailsRepository bankDetailsRepository;

    @Autowired
    private LandDetailsRepository landDetailsRepository;

    @Autowired
    private CropRepository cropRepository;

    public FarmerDTO saveFarmer(FarmerDTO dto) {
        Farmer farmer = FarmerMapper.toEntity(dto);

        // Save Farmer first to generate ID
        farmer = farmerRepository.save(farmer);

        // Save bank details
        if (dto.getBankDetails() != null) {
            List<BankDetails> bankEntities = new ArrayList<>();
            for (BankDetailsDTO bdDto : dto.getBankDetails()) {
                BankDetails bankDetails = new BankDetails();
                bankDetails.setFarmer(farmer);
                bankDetails.setAccountNumber(bdDto.getAccountNumber());
                bankDetails.setBankName(BankName.valueOf(bdDto.getBankName()));
                bankDetails.setIfscCode(bdDto.getIfscCode());
                bankEntities.add(bankDetailsRepository.save(bankDetails));
            }
            farmer.setBankDetails(bankEntities);
        }

        // Save land details
        if (dto.getLandDetails() != null) {
            List<LandDetails> landEntities = new ArrayList<>();
            for (LandDetailsDTO ldDto : dto.getLandDetails()) {
                LandDetails landDetails = new LandDetails();
                landDetails.setFarmer(farmer);
                landDetails.setArea(ldDto.getArea());
                landDetails.setSurveyNumber(ldDto.getSurveyNumber());
                landEntities.add(landDetailsRepository.save(landDetails));
            }
            farmer.setLandDetails(landEntities);
        }

        // Save crops
        if (dto.getCrops() != null) {
            List<Crop> cropEntities = new ArrayList<>();
            for (CropDTO cropDto : dto.getCrops()) {
                Crop crop = new Crop();
                crop.setFarmer(farmer);
                crop.setCropName(cropDto.getCropName());
                crop.setCropType(cropDto.getCropType());
                cropEntities.add(cropRepository.save(crop));
            }
            farmer.setCrops(cropEntities);
        }

        // Final save after setting children
        Farmer savedFarmer = farmerRepository.save(farmer);

        return FarmerMapper.toDto(savedFarmer);
    }

    public FarmerDTO getFarmerById(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new FarmerNotFoundException("Farmer not found with id: " + id));
        return FarmerMapper.toDto(farmer);
    }

    public List<FarmerDTO> getAllFarmers() {
        List<Farmer> farmers = farmerRepository.findAll();
        List<FarmerDTO> dtos = new ArrayList<>();
        for (Farmer farmer : farmers) {
            dtos.add(FarmerMapper.toDto(farmer));
        }
        return dtos;
    }

    public void deleteFarmer(Long id) {
        if (!farmerRepository.existsById(id)) {
            throw new FarmerNotFoundException("Cannot delete. Farmer not found with id: " + id);
        }
        farmerRepository.deleteById(id);
    }
}
