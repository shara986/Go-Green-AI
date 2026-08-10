package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.PlantDiaryEntryRequestDto;
import com.gogreen.ai.dto.response.PlantDiaryEntryResponseDto;
import com.gogreen.ai.entity.PlantDiaryEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface PlantDiaryEntryMapper {

    @Mapping(source = "diary.id", target = "diaryId")
    PlantDiaryEntryResponseDto toResponseDto(PlantDiaryEntry entry);

    List<PlantDiaryEntryResponseDto> toResponseDtoList(List<PlantDiaryEntry> entries);

    @Mapping(target = "diary", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PlantDiaryEntry toEntity(PlantDiaryEntryRequestDto dto);
}
