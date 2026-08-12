package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.PlantDiaryRequestDto;
import com.gogreen.ai.dto.response.PlantDiaryResponseDto;
import com.gogreen.ai.entity.Plant;
import com.gogreen.ai.entity.PlantDiary;
import com.gogreen.ai.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-12T14:58:57+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class PlantDiaryMapperImpl implements PlantDiaryMapper {

    @Autowired
    private PlantDiaryEntryMapper plantDiaryEntryMapper;

    @Override
    public PlantDiaryResponseDto toResponseDto(PlantDiary plantDiary) {
        if ( plantDiary == null ) {
            return null;
        }

        PlantDiaryResponseDto plantDiaryResponseDto = new PlantDiaryResponseDto();

        plantDiaryResponseDto.setUserId( plantDiaryUserId( plantDiary ) );
        plantDiaryResponseDto.setPlantId( plantDiaryPlantId( plantDiary ) );
        plantDiaryResponseDto.setCreatedAt( plantDiary.getCreatedAt() );
        plantDiaryResponseDto.setDateStarted( plantDiary.getDateStarted() );
        plantDiaryResponseDto.setDescription( plantDiary.getDescription() );
        plantDiaryResponseDto.setEntries( plantDiaryEntryMapper.toResponseDtoList( plantDiary.getEntries() ) );
        plantDiaryResponseDto.setId( plantDiary.getId() );
        plantDiaryResponseDto.setPlantName( plantDiary.getPlantName() );
        plantDiaryResponseDto.setUpdatedAt( plantDiary.getUpdatedAt() );

        return plantDiaryResponseDto;
    }

    @Override
    public List<PlantDiaryResponseDto> toResponseDtoList(List<PlantDiary> plantDiaries) {
        if ( plantDiaries == null ) {
            return null;
        }

        List<PlantDiaryResponseDto> list = new ArrayList<PlantDiaryResponseDto>( plantDiaries.size() );
        for ( PlantDiary plantDiary : plantDiaries ) {
            list.add( toResponseDto( plantDiary ) );
        }

        return list;
    }

    @Override
    public PlantDiary toEntity(PlantDiaryRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        PlantDiary plantDiary = new PlantDiary();

        plantDiary.setDateStarted( dto.getDateStarted() );
        plantDiary.setDescription( dto.getDescription() );
        plantDiary.setPlantName( dto.getPlantName() );

        return plantDiary;
    }

    private UUID plantDiaryUserId(PlantDiary plantDiary) {
        if ( plantDiary == null ) {
            return null;
        }
        User user = plantDiary.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID plantDiaryPlantId(PlantDiary plantDiary) {
        if ( plantDiary == null ) {
            return null;
        }
        Plant plant = plantDiary.getPlant();
        if ( plant == null ) {
            return null;
        }
        UUID id = plant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
