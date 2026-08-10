package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.NurseryRequestDto;
import com.gogreen.ai.dto.response.NurseryResponseDto;
import com.gogreen.ai.entity.Nursery;
import com.gogreen.ai.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-10T19:23:48+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class NurseryMapperImpl implements NurseryMapper {

    @Override
    public NurseryResponseDto toResponseDto(Nursery nursery) {
        if ( nursery == null ) {
            return null;
        }

        NurseryResponseDto nurseryResponseDto = new NurseryResponseDto();

        nurseryResponseDto.setUserId( nurseryUserId( nursery ) );
        nurseryResponseDto.setAddress( nursery.getAddress() );
        nurseryResponseDto.setApprovalStatus( nursery.getApprovalStatus() );
        nurseryResponseDto.setCity( nursery.getCity() );
        nurseryResponseDto.setContactEmail( nursery.getContactEmail() );
        nurseryResponseDto.setContactPhone( nursery.getContactPhone() );
        nurseryResponseDto.setCreatedAt( nursery.getCreatedAt() );
        nurseryResponseDto.setDescription( nursery.getDescription() );
        nurseryResponseDto.setId( nursery.getId() );
        nurseryResponseDto.setLogoUrl( nursery.getLogoUrl() );
        nurseryResponseDto.setName( nursery.getName() );
        nurseryResponseDto.setPostalCode( nursery.getPostalCode() );
        nurseryResponseDto.setRating( nursery.getRating() );
        nurseryResponseDto.setUpdatedAt( nursery.getUpdatedAt() );
        nurseryResponseDto.setVerified( nursery.isVerified() );

        return nurseryResponseDto;
    }

    @Override
    public List<NurseryResponseDto> toResponseDtoList(List<Nursery> nurseries) {
        if ( nurseries == null ) {
            return null;
        }

        List<NurseryResponseDto> list = new ArrayList<NurseryResponseDto>( nurseries.size() );
        for ( Nursery nursery : nurseries ) {
            list.add( toResponseDto( nursery ) );
        }

        return list;
    }

    @Override
    public Nursery toEntity(NurseryRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Nursery nursery = new Nursery();

        nursery.setName( dto.getName() );
        nursery.setDescription( dto.getDescription() );
        nursery.setAddress( dto.getAddress() );
        nursery.setCity( dto.getCity() );
        nursery.setPostalCode( dto.getPostalCode() );
        nursery.setContactEmail( dto.getContactEmail() );
        nursery.setContactPhone( dto.getContactPhone() );
        nursery.setRating( dto.getRating() );
        nursery.setVerified( dto.isVerified() );
        nursery.setLogoUrl( dto.getLogoUrl() );

        return nursery;
    }

    private UUID nurseryUserId(Nursery nursery) {
        if ( nursery == null ) {
            return null;
        }
        User user = nursery.getUser();
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
