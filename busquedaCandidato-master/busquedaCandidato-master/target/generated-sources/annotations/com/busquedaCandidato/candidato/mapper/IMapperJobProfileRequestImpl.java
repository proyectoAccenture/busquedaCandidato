package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.JobProfileRequestDto;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-26T16:24:59-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class IMapperJobProfileRequestImpl implements IMapperJobProfileRequest {

    @Override
    public JobProfileEntity JobProfileResquestToStatus(JobProfileRequestDto jobProfileRequestDto) {
        if ( jobProfileRequestDto == null ) {
            return null;
        }

        JobProfileEntity jobProfileEntity = new JobProfileEntity();

        jobProfileEntity.setName( jobProfileRequestDto.getName() );

        return jobProfileEntity;
    }
}
