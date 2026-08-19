package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.RoleRequestDto;
import com.gogreen.ai.dto.response.RoleResponseDto;
import com.gogreen.ai.entity.Role;
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
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleResponseDto toResponseDto(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleResponseDto roleResponseDto = new RoleResponseDto();

        roleResponseDto.setId( role.getId() );
        roleResponseDto.setName( role.getName() );
        roleResponseDto.setCreatedAt( role.getCreatedAt() );
        roleResponseDto.setUpdatedAt( role.getUpdatedAt() );

        return roleResponseDto;
    }

    @Override
    public List<RoleResponseDto> toResponseDtoList(List<Role> roles) {
        if ( roles == null ) {
            return null;
        }

        List<RoleResponseDto> list = new ArrayList<RoleResponseDto>( roles.size() );
        for ( Role role : roles ) {
            list.add( toResponseDto( role ) );
        }

        return list;
    }

    @Override
    public Role toEntity(RoleRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Role role = new Role();

        role.setName( dto.getName() );

        return role;
    }
}
