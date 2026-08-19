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
    date = "2026-08-15T21:49:07+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
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
        notificationResponseDto.setId( notification.getId() );
        notificationResponseDto.setType( notification.getType() );
        notificationResponseDto.setTitle( notification.getTitle() );
        notificationResponseDto.setMessage( notification.getMessage() );
        notificationResponseDto.setRead( notification.isRead() );
        notificationResponseDto.setReadAt( notification.getReadAt() );
        notificationResponseDto.setCreatedAt( notification.getCreatedAt() );
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

        notification.setType( dto.getType() );
        notification.setTitle( dto.getTitle() );
        notification.setMessage( dto.getMessage() );
        notification.setRead( dto.isRead() );

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
