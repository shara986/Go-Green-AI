package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.PlantRequestDto;
import com.gogreen.ai.dto.response.PlantResponseDto;
import com.gogreen.ai.entity.Plant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface PlantMapper {

    @Mapping(source = "nursery.id", target = "nurseryId")
    @Mapping(source = "nursery.name", target = "nurseryName")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    PlantResponseDto toResponseDto(Plant plant);

    List<PlantResponseDto> toResponseDtoList(List<Plant> plants);

    @Mapping(target = "nursery", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Plant toEntity(PlantRequestDto dto);
}
