package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.NotificationRequestDto;
import com.gogreen.ai.dto.response.NotificationResponseDto;
import com.gogreen.ai.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    NotificationResponseDto toResponseDto(Notification notification);

    List<NotificationResponseDto> toResponseDtoList(List<Notification> notifications);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "readAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Notification toEntity(NotificationRequestDto dto);
}
