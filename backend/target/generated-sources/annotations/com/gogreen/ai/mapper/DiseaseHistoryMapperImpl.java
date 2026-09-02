package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.DiseaseHistoryRequestDto;
import com.gogreen.ai.dto.response.DiseaseHistoryResponseDto;
import com.gogreen.ai.entity.DiseaseHistory;
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
public class DiseaseHistoryMapperImpl implements DiseaseHistoryMapper {

    @Override
    public DiseaseHistoryResponseDto toResponseDto(DiseaseHistory diseaseHistory) {
        if ( diseaseHistory == null ) {
            return null;
        }

        DiseaseHistoryResponseDto diseaseHistoryResponseDto = new DiseaseHistoryResponseDto();

        diseaseHistoryResponseDto.setUserId( diseaseHistoryUserId( diseaseHistory ) );
        diseaseHistoryResponseDto.setConfidenceScore( diseaseHistory.getConfidenceScore() );
        diseaseHistoryResponseDto.setCreatedAt( diseaseHistory.getCreatedAt() );
        diseaseHistoryResponseDto.setDateIdentified( diseaseHistory.getDateIdentified() );
        diseaseHistoryResponseDto.setDiseaseIdentified( diseaseHistory.getDiseaseIdentified() );
        diseaseHistoryResponseDto.setId( diseaseHistory.getId() );
        diseaseHistoryResponseDto.setImageUrl( diseaseHistory.getImageUrl() );
        diseaseHistoryResponseDto.setPlantName( diseaseHistory.getPlantName() );
        diseaseHistoryResponseDto.setRecommendedAction( diseaseHistory.getRecommendedAction() );
        diseaseHistoryResponseDto.setSeverity( diseaseHistory.getSeverity() );
        diseaseHistoryResponseDto.setUpdatedAt( diseaseHistory.getUpdatedAt() );

        return diseaseHistoryResponseDto;
    }

    @Override
    public List<DiseaseHistoryResponseDto> toResponseDtoList(List<DiseaseHistory> diseaseHistories) {
        if ( diseaseHistories == null ) {
            return null;
        }

        List<DiseaseHistoryResponseDto> list = new ArrayList<DiseaseHistoryResponseDto>( diseaseHistories.size() );
        for ( DiseaseHistory diseaseHistory : diseaseHistories ) {
            list.add( toResponseDto( diseaseHistory ) );
        }

        return list;
    }

    @Override
    public DiseaseHistory toEntity(DiseaseHistoryRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        DiseaseHistory diseaseHistory = new DiseaseHistory();

        diseaseHistory.setConfidenceScore( dto.getConfidenceScore() );
        diseaseHistory.setDateIdentified( dto.getDateIdentified() );
        diseaseHistory.setDiseaseIdentified( dto.getDiseaseIdentified() );
        diseaseHistory.setImageUrl( dto.getImageUrl() );
        diseaseHistory.setPlantName( dto.getPlantName() );
        diseaseHistory.setRecommendedAction( dto.getRecommendedAction() );
        diseaseHistory.setSeverity( dto.getSeverity() );

        return diseaseHistory;
    }

    private UUID diseaseHistoryUserId(DiseaseHistory diseaseHistory) {
        if ( diseaseHistory == null ) {
            return null;
        }
        User user = diseaseHistory.getUser();
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
