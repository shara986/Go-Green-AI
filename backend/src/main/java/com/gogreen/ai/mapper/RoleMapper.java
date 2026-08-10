package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.RoleRequestDto;
import com.gogreen.ai.dto.response.RoleResponseDto;
import com.gogreen.ai.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface RoleMapper {

    RoleResponseDto toResponseDto(Role role);

    List<RoleResponseDto> toResponseDtoList(List<Role> roles);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Role toEntity(RoleRequestDto dto);
}
