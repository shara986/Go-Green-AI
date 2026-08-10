package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.DiseaseHistoryRequestDto;
import com.gogreen.ai.dto.response.DiseaseHistoryResponseDto;
import com.gogreen.ai.entity.DiseaseHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface DiseaseHistoryMapper {

    @Mapping(source = "user.id", target = "userId")
    DiseaseHistoryResponseDto toResponseDto(DiseaseHistory diseaseHistory);

    List<DiseaseHistoryResponseDto> toResponseDtoList(List<DiseaseHistory> diseaseHistories);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DiseaseHistory toEntity(DiseaseHistoryRequestDto dto);
}
