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
    date = "2026-09-01T23:00:34+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
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
        consultationResponseDto.setId( consultation.getId() );
        consultationResponseDto.setSubject( consultation.getSubject() );
        consultationResponseDto.setStatus( consultation.getStatus() );
        consultationResponseDto.setScheduledAt( consultation.getScheduledAt() );
        consultationResponseDto.setDurationMinutes( consultation.getDurationMinutes() );
        consultationResponseDto.setNotes( consultation.getNotes() );
        consultationResponseDto.setMeetingLink( consultation.getMeetingLink() );
        consultationResponseDto.setCreatedAt( consultation.getCreatedAt() );
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

        consultation.setSubject( dto.getSubject() );
        consultation.setStatus( dto.getStatus() );
        consultation.setScheduledAt( dto.getScheduledAt() );
        consultation.setDurationMinutes( dto.getDurationMinutes() );
        consultation.setNotes( dto.getNotes() );
        consultation.setMeetingLink( dto.getMeetingLink() );

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
