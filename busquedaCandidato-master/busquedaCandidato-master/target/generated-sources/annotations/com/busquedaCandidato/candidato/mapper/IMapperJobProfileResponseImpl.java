package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-26T16:24:59-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class IMapperJobProfileResponseImpl implements IMapperJobProfileResponse {

    @Override
    public JobProfileResponseDto JobProfileToStatusResponse(JobProfileEntity jobProfileEntity) {
        if ( jobProfileEntity == null ) {
            return null;
        }

        String name = null;
        Long id = null;

        name = jobProfileEntity.getName();
        id = jobProfileEntity.getId();

        JobProfileResponseDto jobProfileResponseDto = new JobProfileResponseDto( id, name );

        return jobProfileResponseDto;
    }
}
