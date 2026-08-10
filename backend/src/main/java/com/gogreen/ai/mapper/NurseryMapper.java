package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.NurseryRequestDto;
import com.gogreen.ai.dto.response.NurseryResponseDto;
import com.gogreen.ai.entity.Nursery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface NurseryMapper {

    @Mapping(source = "user.id", target = "userId")
    NurseryResponseDto toResponseDto(Nursery nursery);

    List<NurseryResponseDto> toResponseDtoList(List<Nursery> nurseries);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Nursery toEntity(NurseryRequestDto dto);
}
