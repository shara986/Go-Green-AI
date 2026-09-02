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
    date = "2026-09-02T08:01:45+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
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
        diseaseHistoryResponseDto.setId( diseaseHistory.getId() );
        diseaseHistoryResponseDto.setPlantName( diseaseHistory.getPlantName() );
        diseaseHistoryResponseDto.setDiseaseIdentified( diseaseHistory.getDiseaseIdentified() );
        diseaseHistoryResponseDto.setDateIdentified( diseaseHistory.getDateIdentified() );
        diseaseHistoryResponseDto.setSeverity( diseaseHistory.getSeverity() );
        diseaseHistoryResponseDto.setConfidenceScore( diseaseHistory.getConfidenceScore() );
        diseaseHistoryResponseDto.setRecommendedAction( diseaseHistory.getRecommendedAction() );
        diseaseHistoryResponseDto.setImageUrl( diseaseHistory.getImageUrl() );
        diseaseHistoryResponseDto.setCreatedAt( diseaseHistory.getCreatedAt() );
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

        diseaseHistory.setPlantName( dto.getPlantName() );
        diseaseHistory.setDiseaseIdentified( dto.getDiseaseIdentified() );
        diseaseHistory.setDateIdentified( dto.getDateIdentified() );
        diseaseHistory.setSeverity( dto.getSeverity() );
        diseaseHistory.setConfidenceScore( dto.getConfidenceScore() );
        diseaseHistory.setRecommendedAction( dto.getRecommendedAction() );
        diseaseHistory.setImageUrl( dto.getImageUrl() );

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
