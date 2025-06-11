package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.CropDto;
import com.farmer.farmermanagement.dto.FarmerDto;
import com.farmer.farmermanagement.entity.Crop;
import com.farmer.farmermanagement.entity.Farmer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T08:28:01+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class FarmerMapperImpl implements FarmerMapper {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private BankDetailsMapper bankDetailsMapper;
    @Autowired
    private LandDetailsMapper landDetailsMapper;
    @Autowired
    private CropMapper cropMapper;

    @Override
    public Farmer toEntity(FarmerDto dto) {
        if ( dto == null ) {
            return null;
        }

        Farmer farmer = new Farmer();

        farmer.setAddress( addressMapper.toEntity( dto.getAddress() ) );
        farmer.setBankDetails( bankDetailsMapper.toEntity( dto.getBankDetails() ) );
        farmer.setLandDetails( landDetailsMapper.toEntity( dto.getLandDetails() ) );
        farmer.setCrops( cropDtoListToCropList( dto.getCrops() ) );
        farmer.setAadharNumber( dto.getAadharNumber() );
        farmer.setDateOfBirth( dto.getDateOfBirth() );
        farmer.setDocument( dto.getDocument() );
        farmer.setDocumentPath( dto.getDocumentPath() );
        farmer.setEducation( dto.getEducation() );
        farmer.setEmail( dto.getEmail() );
        farmer.setFarmerType( dto.getFarmerType() );
        farmer.setFirstName( dto.getFirstName() );
        farmer.setGender( dto.getGender() );
        farmer.setId( dto.getId() );
        farmer.setLastName( dto.getLastName() );
        farmer.setMiddleName( dto.getMiddleName() );
        farmer.setPhoneNumber( dto.getPhoneNumber() );
        farmer.setPortalAccess( dto.getPortalAccess() );
        farmer.setPortalRole( dto.getPortalRole() );

        return farmer;
    }

    @Override
    public FarmerDto toDto(Farmer entity) {
        if ( entity == null ) {
            return null;
        }

        FarmerDto farmerDto = new FarmerDto();

        farmerDto.setAddress( addressMapper.toDto( entity.getAddress() ) );
        farmerDto.setBankDetails( bankDetailsMapper.toDto( entity.getBankDetails() ) );
        farmerDto.setLandDetails( landDetailsMapper.toDto( entity.getLandDetails() ) );
        farmerDto.setCrops( cropListToCropDtoList( entity.getCrops() ) );
        farmerDto.setAadharNumber( entity.getAadharNumber() );
        farmerDto.setDateOfBirth( entity.getDateOfBirth() );
        farmerDto.setDocument( entity.getDocument() );
        farmerDto.setDocumentPath( entity.getDocumentPath() );
        farmerDto.setEducation( entity.getEducation() );
        farmerDto.setEmail( entity.getEmail() );
        farmerDto.setFarmerType( entity.getFarmerType() );
        farmerDto.setFirstName( entity.getFirstName() );
        farmerDto.setGender( entity.getGender() );
        farmerDto.setId( entity.getId() );
        farmerDto.setLastName( entity.getLastName() );
        farmerDto.setMiddleName( entity.getMiddleName() );
        farmerDto.setPhoneNumber( entity.getPhoneNumber() );
        farmerDto.setPortalAccess( entity.getPortalAccess() );
        farmerDto.setPortalRole( entity.getPortalRole() );

        return farmerDto;
    }

    protected List<Crop> cropDtoListToCropList(List<CropDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Crop> list1 = new ArrayList<Crop>( list.size() );
        for ( CropDto cropDto : list ) {
            list1.add( cropMapper.toEntity( cropDto ) );
        }

        return list1;
    }

    protected List<CropDto> cropListToCropDtoList(List<Crop> list) {
        if ( list == null ) {
            return null;
        }

        List<CropDto> list1 = new ArrayList<CropDto>( list.size() );
        for ( Crop crop : list ) {
            list1.add( cropMapper.toDto( crop ) );
        }

        return list1;
    }
}
