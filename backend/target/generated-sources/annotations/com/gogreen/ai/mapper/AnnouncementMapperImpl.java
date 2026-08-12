package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.AnnouncementRequestDto;
import com.gogreen.ai.dto.response.AnnouncementResponseDto;
import com.gogreen.ai.entity.Announcement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-12T14:58:58+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class AnnouncementMapperImpl implements AnnouncementMapper {

    @Override
    public AnnouncementResponseDto toResponseDto(Announcement announcement) {
        if ( announcement == null ) {
            return null;
        }

        AnnouncementResponseDto announcementResponseDto = new AnnouncementResponseDto();

        announcementResponseDto.setActive( announcement.isActive() );
        announcementResponseDto.setCreatedAt( announcement.getCreatedAt() );
        announcementResponseDto.setId( announcement.getId() );
        announcementResponseDto.setMessage( announcement.getMessage() );
        announcementResponseDto.setTitle( announcement.getTitle() );
        announcementResponseDto.setUpdatedAt( announcement.getUpdatedAt() );

        return announcementResponseDto;
    }

    @Override
    public List<AnnouncementResponseDto> toResponseDtoList(List<Announcement> announcements) {
        if ( announcements == null ) {
            return null;
        }

        List<AnnouncementResponseDto> list = new ArrayList<AnnouncementResponseDto>( announcements.size() );
        for ( Announcement announcement : announcements ) {
            list.add( toResponseDto( announcement ) );
        }

        return list;
    }

    @Override
    public Announcement toEntity(AnnouncementRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Announcement announcement = new Announcement();

        if ( dto.getActive() != null ) {
            announcement.setActive( dto.getActive() );
        }
        announcement.setMessage( dto.getMessage() );
        announcement.setTitle( dto.getTitle() );

        return announcement;
    }
}
