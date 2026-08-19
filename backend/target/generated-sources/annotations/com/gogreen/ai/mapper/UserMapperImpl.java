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
    date = "2026-08-15T21:49:07+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
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
        userResponseDto.setId( user.getId() );
        userResponseDto.setName( user.getName() );
        userResponseDto.setUsername( user.getUsername() );
        userResponseDto.setEmail( user.getEmail() );
        userResponseDto.setPhoneNumber( user.getPhoneNumber() );
        userResponseDto.setEnabled( user.isEnabled() );
        userResponseDto.setApprovalStatus( user.getApprovalStatus() );
        userResponseDto.setDeleted( user.isDeleted() );
        userResponseDto.setCreatedAt( user.getCreatedAt() );
        userResponseDto.setUpdatedAt( user.getUpdatedAt() );

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

        user.setName( dto.getName() );
        user.setUsername( dto.getUsername() );
        user.setEmail( dto.getEmail() );
        user.setPassword( dto.getPassword() );
        user.setPhoneNumber( dto.getPhoneNumber() );
        user.setEnabled( dto.isEnabled() );

        return user;
    }
}
