package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.AnnouncementRequestDto;
import com.gogreen.ai.dto.response.AnnouncementResponseDto;
import com.gogreen.ai.entity.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface AnnouncementMapper {

    AnnouncementResponseDto toResponseDto(Announcement announcement);

    List<AnnouncementResponseDto> toResponseDtoList(List<Announcement> announcements);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Announcement toEntity(AnnouncementRequestDto dto);
}
