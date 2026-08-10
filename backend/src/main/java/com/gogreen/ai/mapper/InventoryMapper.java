package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.InventoryRequestDto;
import com.gogreen.ai.dto.response.InventoryResponseDto;
import com.gogreen.ai.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface InventoryMapper {

    @Mapping(source = "plant.id", target = "plantId")
    @Mapping(source = "plant.name", target = "plantName")
    InventoryResponseDto toResponseDto(Inventory inventory);

    List<InventoryResponseDto> toResponseDtoList(List<Inventory> inventories);

    @Mapping(target = "plant", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Inventory toEntity(InventoryRequestDto dto);
}
