package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.UserRequestDto;
import com.gogreen.ai.dto.response.UserResponseDto;
import com.gogreen.ai.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-10T19:23:48+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDto toResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setRoles( rolesToNames( user.getRoles() ) );
        userResponseDto.setApprovalStatus( user.getApprovalStatus() );
        userResponseDto.setCreatedAt( user.getCreatedAt() );
        userResponseDto.setDeleted( user.isDeleted() );
        userResponseDto.setEmail( user.getEmail() );
        userResponseDto.setEnabled( user.isEnabled() );
        userResponseDto.setId( user.getId() );
        userResponseDto.setName( user.getName() );
        userResponseDto.setPhoneNumber( user.getPhoneNumber() );
        userResponseDto.setUpdatedAt( user.getUpdatedAt() );
        userResponseDto.setUsername( user.getUsername() );

        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> toResponseDtoList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( users.size() );
        for ( User user : users ) {
            list.add( toResponseDto( user ) );
        }

        return list;
    }

    @Override
    public User toEntity(UserRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( dto.getEmail() );
        user.setEnabled( dto.isEnabled() );
        user.setName( dto.getName() );
        user.setPassword( dto.getPassword() );
        user.setPhoneNumber( dto.getPhoneNumber() );
        user.setUsername( dto.getUsername() );

        return user;
    }
}
