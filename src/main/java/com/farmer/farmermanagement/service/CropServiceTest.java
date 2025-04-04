package com.farmer.farmermanagement.service;

import com.farmer.farmermanagement.dto.CropDTO;
import com.farmer.farmermanagement.entity.Crop;
import com.farmer.farmermanagement.entity.Farmer;
import com.farmer.farmermanagement.exception.FarmerNotFoundException;
import com.farmer.farmermanagement.exception.ResourceNotFoundException;
import com.farmer.farmermanagement.mapper.CropMapper;
import com.farmer.farmermanagement.repository.CropRepository;
import com.farmer.farmermanagement.repository.FarmerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CropServiceTest {

    @Mock
    private CropRepository cropRepository;

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private CropMapper cropMapper;

    @InjectMocks
    private CropService cropService;

    private CropDTO cropDTO;
    private Crop crop;
    private Farmer farmer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        farmer = new Farmer();
        farmer.setId(1L);
        farmer.setFirstName("John");
        farmer.setMiddleName("Doe");
        farmer.setLastName("Smith");

        cropDTO = new CropDTO();
        cropDTO.setId(1L);
        cropDTO.setFarmerId(1L);
        cropDTO.setCropName("Cotton");

        crop = new Crop();
        crop.setId(1L);
        crop.setFarmer(farmer);
        crop.setCropName("Cotton");
    }

    @Test
    void addCropshouldReturnCropDTO() {
        when(farmerRepository.findById(1L)).thenReturn(Optional.of(farmer));
        when(cropMapper.toCropEntity(cropDTO, farmer)).thenReturn(crop);
        when(cropRepository.save(crop)).thenReturn(crop);
        when(cropMapper.toCropDTO(crop)).thenReturn(cropDTO);

        CropDTO result = cropService.addCrop(cropDTO);

        assertNotNull(result);
        assertEquals(cropDTO.getCropName(), result.getCropName());
        verify(farmerRepository).findById(1L);
        verify(cropRepository).save(crop);
    }

    @Test
    void addCropFarmerNotFoundShouldThrowFarmerNotFoundException() {
        when(farmerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FarmerNotFoundException.class, () -> cropService.addCrop(cropDTO));
    }

    @Test
    void getCropByIdShouldReturnCropDTO() {
        when(cropRepository.findById(1L)).thenReturn(Optional.of(crop));
        when(cropMapper.toCropDTO(crop)).thenReturn(cropDTO);

        CropDTO result = cropService.getCropById(1L);

        assertNotNull(result);
        assertEquals(cropDTO.getId(), result.getId());
        verify(cropRepository).findById(1L);
    }

    @Test
    void getCropByIdCropNotFoundShouldThrowResourceNotFoundException() {
        when(cropRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cropService.getCropById(1L));
    }

    @Test
    void getCropsByFarmerIdShouldReturnListOfCropDTOs() {
        when(cropRepository.findByFarmerId(1L)).thenReturn(List.of(crop));
        when(cropMapper.toCropDTO(crop)).thenReturn(cropDTO);

        List<CropDTO> result = cropService.getCropsByFarmerId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(cropRepository).findByFarmerId(1L);
    }

    @Test
    void deleteCropShouldDeleteCrop() {
        when(cropRepository.findById(1L)).thenReturn(Optional.of(crop));

        cropService.deleteCrop(1L);

        verify(cropRepository).delete(crop);
    }

    @Test
    void deleteCropCropNotFoundShouldThrowResourceNotFoundException() {
        when(cropRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cropService.deleteCrop(1L));
    }
}
