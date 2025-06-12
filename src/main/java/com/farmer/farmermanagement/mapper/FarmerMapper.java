package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.FarmerDTO;
import com.farmer.farmermanagement.dto.BankDetailsDTO;
import com.farmer.farmermanagement.dto.LandDetailsDTO;
import com.farmer.farmermanagement.dto.CropDTO;
import com.farmer.farmermanagement.entity.BankDetails;
import com.farmer.farmermanagement.entity.Crop;
import com.farmer.farmermanagement.entity.Farmer;
import com.farmer.farmermanagement.entity.LandDetails;

import java.util.List;
import java.util.stream.Collectors;

public class FarmerMapper {

    public static FarmerDTO toDto(Farmer farmer) {
        FarmerDTO dto = new FarmerDTO();
        dto.setId(farmer.getId());
        dto.setPhoto(farmer.getPhoto());
        dto.setSalutation(farmer.getSalutation());
        dto.setFirstName(farmer.getFirstName());
        dto.setMiddleName(farmer.getMiddleName());
        dto.setLastName(farmer.getLastName());
        dto.setGender(farmer.getGender());
        dto.setNationality(farmer.getNationality());
        dto.setDateOfBirth(farmer.getDob());
        dto.setPhoneNumber(farmer.getContactNumber());
        dto.setFatherName(farmer.getFatherName());
        dto.setAlternativeNumber(farmer.getAlternativeNumber());
        dto.setAlternativeNoType(farmer.getAlternativeNoType());
        dto.setRelationshipType(farmer.getRelationshipType());
        dto.setCountry(farmer.getCountry());
        dto.setState(farmer.getState());
        dto.setDistrict(farmer.getDistrict());
        dto.setBlock(farmer.getBlock());
        dto.setVillage(farmer.getVillage());
        dto.setZipcode(farmer.getZipcode());
        dto.setEducation(farmer.getEducation());
        dto.setDocument(farmer.getDocument());
        dto.setFarmingExperience(farmer.getFarmingExperience());
        dto.setNetIncome(farmer.getNetIncome());
        dto.setFarmerType(farmer.getFarmerType());
        dto.setPortalRole(farmer.getPortalRole());
        dto.setPortalAccess(farmer.getPortalAccess());

        if (farmer.getBankDetails() != null) {
            dto.setBankDetails(farmer.getBankDetails().stream()
                    .map(bank -> {
                        BankDetailsDTO bankDto = new BankDetailsDTO();
                        bankDto.setId(bank.getId());
                        bankDto.setAccountNumber(bank.getAccountNumber());
                        bankDto.setIfscCode(bank.getIfscCode());
                        bankDto.setBankName(bank.getBankName());
                        return bankDto;
                    }).collect(Collectors.toList()));
        }

        if (farmer.getLandDetails() != null) {
            dto.setLandDetails(farmer.getLandDetails().stream()
                    .map(land -> {
                        LandDetailsDTO landDto = new LandDetailsDTO();
                        landDto.setId(land.getId());
                        landDto.setSurveyNumber(land.getSurveyNumber());
                        landDto.setArea(land.getArea());
                        landDto.setLandType(land.getLandType());
                        return landDto;
                    }).collect(Collectors.toList()));
        }

        if (farmer.getCrops() != null) {
            dto.setCrops(farmer.getCrops().stream().map(crop -> {
                CropDTO cropDto = new CropDTO();
                cropDto.setId(crop.getId());
                cropDto.setCropName(crop.getCropName());
                cropDto.setSeason(crop.getSeason());
                cropDto.setArea(crop.getArea());
                return cropDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }

    public static Farmer toEntity(FarmerDTO dto) {
        Farmer farmer = new Farmer();
        farmer.setId(dto.getId());
        farmer.setPhoto(dto.getPhoto());
        farmer.setSalutation(dto.getSalutation());
        farmer.setFirstName(dto.getFirstName());
        farmer.setMiddleName(dto.getMiddleName());
        farmer.setLastName(dto.getLastName());
        farmer.setGender(dto.getGender());
        farmer.setNationality(dto.getNationality());
        farmer.setDob(dto.getDateOfBirth());
        farmer.setContactNumber(dto.getPhoneNumber());
        farmer.setFatherName(dto.getFatherName());
        farmer.setAlternativeNumber(dto.getAlternativeNumber());
        farmer.setAlternativeNoType(dto.getAlternativeNoType());
        farmer.setRelationshipType(dto.getRelationshipType());
        farmer.setCountry(dto.getCountry());
        farmer.setState(dto.getState());
        farmer.setDistrict(dto.getDistrict());
        farmer.setBlock(dto.getBlock());
        farmer.setVillage(dto.getVillage());
        farmer.setZipcode(dto.getZipcode());
        farmer.setEducation(dto.getEducation());
        farmer.setDocument(dto.getDocument());
        farmer.setFarmingExperience(dto.getFarmingExperience());
        farmer.setNetIncome(dto.getNetIncome());
        farmer.setFarmerType(dto.getFarmerType());
        farmer.setPortalRole(dto.getPortalRole());
        farmer.setPortalAccess(dto.getPortalAccess());

        if (dto.getBankDetails() != null) {
            List<BankDetails> banks = dto.getBankDetails().stream()
                    .map(bankDto -> {
                        BankDetails bank = new BankDetails();
                        bank.setId(bankDto.getId());
                        bank.setAccountNumber(bankDto.getAccountNumber());
                        bank.setIfscCode(bankDto.getIfscCode());
                        bank.setBankName(bankDto.getBankName());
                        bank.setFarmer(farmer);
                        return bank;
                    }).collect(Collectors.toList());
            farmer.setBankDetails(banks);
        }

        if (dto.getLandDetails() != null) {
            List<LandDetails> lands = dto.getLandDetails().stream()
                    .map(landDto -> {
                        LandDetails land = new LandDetails();
                        land.setId(landDto.getId());
                        land.setSurveyNumber(landDto.getSurveyNumber());
                        land.setArea(landDto.getArea());
                        land.setLandType(landDto.getLandType());
                        land.setFarmer(farmer);
                        return land;
                    }).collect(Collectors.toList());
            farmer.setLandDetails(lands);
        }

        if (dto.getCrops() != null) {
            List<Crop> crops = dto.getCrops().stream()
                    .map(cropDto -> {
                        Crop crop = new Crop();
                        crop.setId(cropDto.getId());
                        crop.setCropName(cropDto.getCropName());
                        crop.setSeason(cropDto.getSeason());
                        crop.setArea(cropDto.getArea());
                        crop.setFarmer(farmer);
                        return crop;
                    }).collect(Collectors.toList());
            farmer.setCrops(crops);
        }

        return farmer;
    }
}
