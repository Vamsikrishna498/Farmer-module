package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.CropDto;
import com.farmer.farmermanagement.entity.Crop;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T08:27:59+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class CropMapperImpl implements CropMapper {

    @Override
    public CropDto toDto(Crop crop) {
        if ( crop == null ) {
            return null;
        }

        CropDto.CropDtoBuilder cropDto = CropDto.builder();

        cropDto.areaInAcres( crop.getAreaInAcres() );
        cropDto.cropName( crop.getCropName() );
        cropDto.cropType( crop.getCropType() );
        cropDto.geoTag( crop.getGeoTag() );
        cropDto.irrigationSource( crop.getIrrigationSource() );
        cropDto.latitude( crop.getLatitude() );
        cropDto.longitude( crop.getLongitude() );
        cropDto.netIncome( crop.getNetIncome() );
        cropDto.photo( crop.getPhoto() );
        cropDto.soilTest( crop.getSoilTest() );
        cropDto.soilTestCertificate( crop.getSoilTestCertificate() );
        cropDto.surveyNumber( crop.getSurveyNumber() );

        return cropDto.build();
    }

    @Override
    public Crop toEntity(CropDto cropDTO) {
        if ( cropDTO == null ) {
            return null;
        }

        Crop.CropBuilder crop = Crop.builder();

        crop.areaInAcres( cropDTO.getAreaInAcres() );
        crop.cropName( cropDTO.getCropName() );
        crop.cropType( cropDTO.getCropType() );
        crop.geoTag( cropDTO.getGeoTag() );
        crop.irrigationSource( cropDTO.getIrrigationSource() );
        crop.latitude( cropDTO.getLatitude() );
        crop.longitude( cropDTO.getLongitude() );
        crop.netIncome( cropDTO.getNetIncome() );
        crop.photo( cropDTO.getPhoto() );
        crop.soilTest( cropDTO.getSoilTest() );
        crop.soilTestCertificate( cropDTO.getSoilTestCertificate() );
        crop.surveyNumber( cropDTO.getSurveyNumber() );

        return crop.build();
    }
}
