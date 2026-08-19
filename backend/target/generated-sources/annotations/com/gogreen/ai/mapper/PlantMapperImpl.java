package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.PlantRequestDto;
import com.gogreen.ai.dto.response.PlantResponseDto;
import com.gogreen.ai.entity.Category;
import com.gogreen.ai.entity.Nursery;
import com.gogreen.ai.entity.Plant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-15T21:49:07+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class PlantMapperImpl implements PlantMapper {

    @Override
    public PlantResponseDto toResponseDto(Plant plant) {
        if ( plant == null ) {
            return null;
        }

        PlantResponseDto plantResponseDto = new PlantResponseDto();

        plantResponseDto.setNurseryId( plantNurseryId( plant ) );
        plantResponseDto.setNurseryName( plantNurseryName( plant ) );
        plantResponseDto.setCategoryId( plantCategoryId( plant ) );
        plantResponseDto.setCategoryName( plantCategoryName( plant ) );
        plantResponseDto.setId( plant.getId() );
        plantResponseDto.setName( plant.getName() );
        plantResponseDto.setScientificName( plant.getScientificName() );
        plantResponseDto.setSku( plant.getSku() );
        plantResponseDto.setDescription( plant.getDescription() );
        plantResponseDto.setCareInstructions( plant.getCareInstructions() );
        plantResponseDto.setPrice( plant.getPrice() );
        plantResponseDto.setStock( plant.getStock() );
        plantResponseDto.setPlantType( plant.getPlantType() );
        plantResponseDto.setImageUrl( plant.getImageUrl() );
        plantResponseDto.setActive( plant.isActive() );
        plantResponseDto.setCreatedAt( plant.getCreatedAt() );
        plantResponseDto.setUpdatedAt( plant.getUpdatedAt() );

        return plantResponseDto;
    }

    @Override
    public List<PlantResponseDto> toResponseDtoList(List<Plant> plants) {
        if ( plants == null ) {
            return null;
        }

        List<PlantResponseDto> list = new ArrayList<PlantResponseDto>( plants.size() );
        for ( Plant plant : plants ) {
            list.add( toResponseDto( plant ) );
        }

        return list;
    }

    @Override
    public Plant toEntity(PlantRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Plant plant = new Plant();

        plant.setName( dto.getName() );
        plant.setScientificName( dto.getScientificName() );
        plant.setSku( dto.getSku() );
        plant.setDescription( dto.getDescription() );
        plant.setCareInstructions( dto.getCareInstructions() );
        plant.setPrice( dto.getPrice() );
        plant.setStock( dto.getStock() );
        plant.setPlantType( dto.getPlantType() );
        plant.setImageUrl( dto.getImageUrl() );
        plant.setActive( dto.isActive() );

        return plant;
    }

    private UUID plantNurseryId(Plant plant) {
        if ( plant == null ) {
            return null;
        }
        Nursery nursery = plant.getNursery();
        if ( nursery == null ) {
            return null;
        }
        UUID id = nursery.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String plantNurseryName(Plant plant) {
        if ( plant == null ) {
            return null;
        }
        Nursery nursery = plant.getNursery();
        if ( nursery == null ) {
            return null;
        }
        String name = nursery.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID plantCategoryId(Plant plant) {
        if ( plant == null ) {
            return null;
        }
        Category category = plant.getCategory();
        if ( category == null ) {
            return null;
        }
        UUID id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String plantCategoryName(Plant plant) {
        if ( plant == null ) {
            return null;
        }
        Category category = plant.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
