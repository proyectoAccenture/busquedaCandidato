package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.JobProfileResponseDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-25T18:55:32-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class IMapperJobProfileResponseImpl implements IMapperJobProfileResponse {

    @Override
    public JobProfileResponseDto JobProfileToJobProfileResponse(JobProfileEntity jobProfileEntityEntity) {
        if ( jobProfileEntityEntity == null ) {
            return null;
        }

        String name = null;
        Long id = null;

        name = jobProfileEntityEntity.getName();
        id = jobProfileEntityEntity.getId();

        JobProfileResponseDto jobProfileResponseDto = new JobProfileResponseDto( id, name );

        return jobProfileResponseDto;
    }
}
