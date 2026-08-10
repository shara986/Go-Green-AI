package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.ConsultationRequestDto;
import com.gogreen.ai.dto.response.ConsultationResponseDto;
import com.gogreen.ai.entity.Consultation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface ConsultationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "expert.id", target = "expertId")
    @Mapping(source = "expert.name", target = "expertName")
    ConsultationResponseDto toResponseDto(Consultation consultation);

    List<ConsultationResponseDto> toResponseDtoList(List<Consultation> consultations);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "expert", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Consultation toEntity(ConsultationRequestDto dto);
}
