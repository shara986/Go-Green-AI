package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.NotificationRequestDto;
import com.gogreen.ai.dto.response.NotificationResponseDto;
import com.gogreen.ai.entity.Notification;
import com.gogreen.ai.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-02T07:58:26+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationResponseDto toResponseDto(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationResponseDto notificationResponseDto = new NotificationResponseDto();

        notificationResponseDto.setUserId( notificationUserId( notification ) );
        notificationResponseDto.setCreatedAt( notification.getCreatedAt() );
        notificationResponseDto.setId( notification.getId() );
        notificationResponseDto.setMessage( notification.getMessage() );
        notificationResponseDto.setRead( notification.isRead() );
        notificationResponseDto.setReadAt( notification.getReadAt() );
        notificationResponseDto.setTitle( notification.getTitle() );
        notificationResponseDto.setType( notification.getType() );
        notificationResponseDto.setUpdatedAt( notification.getUpdatedAt() );

        return notificationResponseDto;
    }

    @Override
    public List<NotificationResponseDto> toResponseDtoList(List<Notification> notifications) {
        if ( notifications == null ) {
            return null;
        }

        List<NotificationResponseDto> list = new ArrayList<NotificationResponseDto>( notifications.size() );
        for ( Notification notification : notifications ) {
            list.add( toResponseDto( notification ) );
        }

        return list;
    }

    @Override
    public Notification toEntity(NotificationRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setMessage( dto.getMessage() );
        notification.setRead( dto.isRead() );
        notification.setTitle( dto.getTitle() );
        notification.setType( dto.getType() );

        return notification;
    }

    private UUID notificationUserId(Notification notification) {
        if ( notification == null ) {
            return null;
        }
        User user = notification.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
