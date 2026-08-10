package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.ConsultationRequestDto;
import com.gogreen.ai.dto.response.ConsultationResponseDto;
import com.gogreen.ai.entity.Consultation;
import com.gogreen.ai.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-10T19:23:47+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ConsultationMapperImpl implements ConsultationMapper {

    @Override
    public ConsultationResponseDto toResponseDto(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }

        ConsultationResponseDto consultationResponseDto = new ConsultationResponseDto();

        consultationResponseDto.setUserId( consultationUserId( consultation ) );
        consultationResponseDto.setUserName( consultationUserName( consultation ) );
        consultationResponseDto.setExpertId( consultationExpertId( consultation ) );
        consultationResponseDto.setExpertName( consultationExpertName( consultation ) );
        consultationResponseDto.setCreatedAt( consultation.getCreatedAt() );
        consultationResponseDto.setDurationMinutes( consultation.getDurationMinutes() );
        consultationResponseDto.setId( consultation.getId() );
        consultationResponseDto.setMeetingLink( consultation.getMeetingLink() );
        consultationResponseDto.setNotes( consultation.getNotes() );
        consultationResponseDto.setScheduledAt( consultation.getScheduledAt() );
        consultationResponseDto.setStatus( consultation.getStatus() );
        consultationResponseDto.setSubject( consultation.getSubject() );
        consultationResponseDto.setUpdatedAt( consultation.getUpdatedAt() );

        return consultationResponseDto;
    }

    @Override
    public List<ConsultationResponseDto> toResponseDtoList(List<Consultation> consultations) {
        if ( consultations == null ) {
            return null;
        }

        List<ConsultationResponseDto> list = new ArrayList<ConsultationResponseDto>( consultations.size() );
        for ( Consultation consultation : consultations ) {
            list.add( toResponseDto( consultation ) );
        }

        return list;
    }

    @Override
    public Consultation toEntity(ConsultationRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Consultation consultation = new Consultation();

        consultation.setDurationMinutes( dto.getDurationMinutes() );
        consultation.setMeetingLink( dto.getMeetingLink() );
        consultation.setNotes( dto.getNotes() );
        consultation.setScheduledAt( dto.getScheduledAt() );
        consultation.setStatus( dto.getStatus() );
        consultation.setSubject( dto.getSubject() );

        return consultation;
    }

    private UUID consultationUserId(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }
        User user = consultation.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String consultationUserName(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }
        User user = consultation.getUser();
        if ( user == null ) {
            return null;
        }
        String name = user.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID consultationExpertId(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }
        User expert = consultation.getExpert();
        if ( expert == null ) {
            return null;
        }
        UUID id = expert.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String consultationExpertName(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }
        User expert = consultation.getExpert();
        if ( expert == null ) {
            return null;
        }
        String name = expert.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
