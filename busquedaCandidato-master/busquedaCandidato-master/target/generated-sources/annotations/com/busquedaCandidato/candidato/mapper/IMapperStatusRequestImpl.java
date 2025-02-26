package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.StateRequestDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-25T18:37:07-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class IMapperStatusRequestImpl implements IMapperStatusRequest {

    @Override
    public StateEntity StatusRequestToStatus(StateRequestDto stateRequestDto) {
        if ( stateRequestDto == null ) {
            return null;
        }

        StateEntity stateEntity = new StateEntity();

        stateEntity.setName( stateRequestDto.getName() );

        return stateEntity;
    }
}
