package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.InventoryRequestDto;
import com.gogreen.ai.dto.response.InventoryResponseDto;
import com.gogreen.ai.entity.Inventory;
import com.gogreen.ai.entity.Plant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-10T19:23:48+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public InventoryResponseDto toResponseDto(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }

        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();

        inventoryResponseDto.setPlantId( inventoryPlantId( inventory ) );
        inventoryResponseDto.setPlantName( inventoryPlantName( inventory ) );
        inventoryResponseDto.setCreatedAt( inventory.getCreatedAt() );
        inventoryResponseDto.setId( inventory.getId() );
        inventoryResponseDto.setLastRestockDate( inventory.getLastRestockDate() );
        inventoryResponseDto.setReorderLevel( inventory.getReorderLevel() );
        inventoryResponseDto.setReservedQuantity( inventory.getReservedQuantity() );
        inventoryResponseDto.setStockLevel( inventory.getStockLevel() );
        inventoryResponseDto.setUpdatedAt( inventory.getUpdatedAt() );

        return inventoryResponseDto;
    }

    @Override
    public List<InventoryResponseDto> toResponseDtoList(List<Inventory> inventories) {
        if ( inventories == null ) {
            return null;
        }

        List<InventoryResponseDto> list = new ArrayList<InventoryResponseDto>( inventories.size() );
        for ( Inventory inventory : inventories ) {
            list.add( toResponseDto( inventory ) );
        }

        return list;
    }

    @Override
    public Inventory toEntity(InventoryRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Inventory inventory = new Inventory();

        inventory.setLastRestockDate( dto.getLastRestockDate() );
        inventory.setReorderLevel( dto.getReorderLevel() );
        inventory.setReservedQuantity( dto.getReservedQuantity() );
        inventory.setStockLevel( dto.getStockLevel() );

        return inventory;
    }

    private UUID inventoryPlantId(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }
        Plant plant = inventory.getPlant();
        if ( plant == null ) {
            return null;
        }
        UUID id = plant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String inventoryPlantName(Inventory inventory) {
        if ( inventory == null ) {
            return null;
        }
        Plant plant = inventory.getPlant();
        if ( plant == null ) {
            return null;
        }
        String name = plant.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
