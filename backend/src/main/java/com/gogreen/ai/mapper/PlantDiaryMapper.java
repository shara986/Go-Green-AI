package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.PlantDiaryRequestDto;
import com.gogreen.ai.dto.response.PlantDiaryResponseDto;
import com.gogreen.ai.entity.PlantDiary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class, uses = PlantDiaryEntryMapper.class)
public interface PlantDiaryMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "plant.id", target = "plantId")
    PlantDiaryResponseDto toResponseDto(PlantDiary plantDiary);

    List<PlantDiaryResponseDto> toResponseDtoList(List<PlantDiary> plantDiaries);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "plant", ignore = true)
    @Mapping(target = "entries", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PlantDiary toEntity(PlantDiaryRequestDto dto);
}
