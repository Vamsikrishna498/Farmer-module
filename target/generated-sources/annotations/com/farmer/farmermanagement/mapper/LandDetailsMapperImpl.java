package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.LandDetailsDto;
import com.farmer.farmermanagement.entity.LandDetails;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T08:27:59+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class LandDetailsMapperImpl implements LandDetailsMapper {

    @Override
    public LandDetailsDto toDto(LandDetails landDetails) {
        if ( landDetails == null ) {
            return null;
        }

        LandDetailsDto landDetailsDto = new LandDetailsDto();

        landDetailsDto.setBorewellDischarge( landDetails.getBorewellDischarge() );
        landDetailsDto.setBorewellLocation( landDetails.getBorewellLocation() );
        landDetailsDto.setCropType( landDetails.getCropType() );
        landDetailsDto.setGeoTag( landDetails.getGeoTag() );
        landDetailsDto.setId( landDetails.getId() );
        landDetailsDto.setIrrigationSource( landDetails.getIrrigationSource() );
        landDetailsDto.setLandSize( landDetails.getLandSize() );
        landDetailsDto.setLatitude( landDetails.getLatitude() );
        landDetailsDto.setLongitude( landDetails.getLongitude() );
        landDetailsDto.setSoilTest( landDetails.getSoilTest() );
        landDetailsDto.setSoilTestCertificate( landDetails.getSoilTestCertificate() );
        landDetailsDto.setSurveyNumber( landDetails.getSurveyNumber() );

        return landDetailsDto;
    }

    @Override
    public LandDetails toEntity(LandDetailsDto landDetailsDto) {
        if ( landDetailsDto == null ) {
            return null;
        }

        LandDetails landDetails = new LandDetails();

        landDetails.setBorewellDischarge( landDetailsDto.getBorewellDischarge() );
        landDetails.setBorewellLocation( landDetailsDto.getBorewellLocation() );
        landDetails.setCropType( landDetailsDto.getCropType() );
        landDetails.setGeoTag( landDetailsDto.getGeoTag() );
        landDetails.setId( landDetailsDto.getId() );
        landDetails.setIrrigationSource( landDetailsDto.getIrrigationSource() );
        landDetails.setLandSize( landDetailsDto.getLandSize() );
        landDetails.setLatitude( landDetailsDto.getLatitude() );
        landDetails.setLongitude( landDetailsDto.getLongitude() );
        landDetails.setSoilTest( landDetailsDto.getSoilTest() );
        landDetails.setSoilTestCertificate( landDetailsDto.getSoilTestCertificate() );
        landDetails.setSurveyNumber( landDetailsDto.getSurveyNumber() );

        return landDetails;
    }
}
