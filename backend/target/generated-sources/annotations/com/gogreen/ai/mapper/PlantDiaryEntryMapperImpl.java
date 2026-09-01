package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.PlantDiaryEntryRequestDto;
import com.gogreen.ai.dto.response.PlantDiaryEntryResponseDto;
import com.gogreen.ai.entity.PlantDiary;
import com.gogreen.ai.entity.PlantDiaryEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-01T22:27:56+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class PlantDiaryEntryMapperImpl implements PlantDiaryEntryMapper {

    @Override
    public PlantDiaryEntryResponseDto toResponseDto(PlantDiaryEntry entry) {
        if ( entry == null ) {
            return null;
        }

        PlantDiaryEntryResponseDto plantDiaryEntryResponseDto = new PlantDiaryEntryResponseDto();

        plantDiaryEntryResponseDto.setDiaryId( entryDiaryId( entry ) );
        plantDiaryEntryResponseDto.setId( entry.getId() );
        plantDiaryEntryResponseDto.setEntryDate( entry.getEntryDate() );
        plantDiaryEntryResponseDto.setNote( entry.getNote() );
        plantDiaryEntryResponseDto.setPhotoUrl( entry.getPhotoUrl() );
        plantDiaryEntryResponseDto.setCreatedAt( entry.getCreatedAt() );
        plantDiaryEntryResponseDto.setUpdatedAt( entry.getUpdatedAt() );

        return plantDiaryEntryResponseDto;
    }

    @Override
    public List<PlantDiaryEntryResponseDto> toResponseDtoList(List<PlantDiaryEntry> entries) {
        if ( entries == null ) {
            return null;
        }

        List<PlantDiaryEntryResponseDto> list = new ArrayList<PlantDiaryEntryResponseDto>( entries.size() );
        for ( PlantDiaryEntry plantDiaryEntry : entries ) {
            list.add( toResponseDto( plantDiaryEntry ) );
        }

        return list;
    }

    @Override
    public PlantDiaryEntry toEntity(PlantDiaryEntryRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        PlantDiaryEntry plantDiaryEntry = new PlantDiaryEntry();

        plantDiaryEntry.setEntryDate( dto.getEntryDate() );
        plantDiaryEntry.setNote( dto.getNote() );
        plantDiaryEntry.setPhotoUrl( dto.getPhotoUrl() );

        return plantDiaryEntry;
    }

    private UUID entryDiaryId(PlantDiaryEntry plantDiaryEntry) {
        if ( plantDiaryEntry == null ) {
            return null;
        }
        PlantDiary diary = plantDiaryEntry.getDiary();
        if ( diary == null ) {
            return null;
        }
        UUID id = diary.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
