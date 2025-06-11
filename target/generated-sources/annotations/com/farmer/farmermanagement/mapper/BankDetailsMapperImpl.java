package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.BankDetailsDto;
import com.farmer.farmermanagement.entity.BankDetails;
import com.farmer.farmermanagement.enums.BankName;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T08:28:00+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class BankDetailsMapperImpl implements BankDetailsMapper {

    @Override
    public BankDetails toEntity(BankDetailsDto dto) {
        if ( dto == null ) {
            return null;
        }

        BankDetails bankDetails = new BankDetails();

        bankDetails.setAccountNumber( dto.getAccountNumber() );
        if ( dto.getBankName() != null ) {
            bankDetails.setBankName( dto.getBankName().name() );
        }
        bankDetails.setIfscCode( dto.getIfscCode() );

        return bankDetails;
    }

    @Override
    public BankDetailsDto toDto(BankDetails entity) {
        if ( entity == null ) {
            return null;
        }

        BankDetailsDto.BankDetailsDtoBuilder bankDetailsDto = BankDetailsDto.builder();

        bankDetailsDto.accountNumber( entity.getAccountNumber() );
        if ( entity.getBankName() != null ) {
            bankDetailsDto.bankName( Enum.valueOf( BankName.class, entity.getBankName() ) );
        }
        bankDetailsDto.ifscCode( entity.getIfscCode() );

        return bankDetailsDto.build();
    }
}
