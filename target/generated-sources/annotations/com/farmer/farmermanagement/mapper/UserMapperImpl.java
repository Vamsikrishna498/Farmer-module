package com.farmer.farmermanagement.mapper;

import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.entity.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-13T08:26:49+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setCountry( user.getCountry() );
        if ( user.getDateOfBirth() != null ) {
            userDTO.setDateOfBirth( DateTimeFormatter.ISO_LOCAL_DATE.format( user.getDateOfBirth() ) );
        }
        userDTO.setEmail( user.getEmail() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setGender( user.getGender() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setPhoneNumber( user.getPhoneNumber() );
        userDTO.setPinCode( user.getPinCode() );
        userDTO.setState( user.getState() );
        userDTO.setTimeZone( user.getTimeZone() );

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.country( userDto.getCountry() );
        if ( userDto.getDateOfBirth() != null ) {
            user.dateOfBirth( LocalDate.parse( userDto.getDateOfBirth() ) );
        }
        user.email( userDto.getEmail() );
        user.firstName( userDto.getFirstName() );
        user.gender( userDto.getGender() );
        user.lastName( userDto.getLastName() );
        user.password( userDto.getPassword() );
        user.phoneNumber( userDto.getPhoneNumber() );
        user.pinCode( userDto.getPinCode() );
        user.state( userDto.getState() );
        user.timeZone( userDto.getTimeZone() );

        return user.build();
    }
}
