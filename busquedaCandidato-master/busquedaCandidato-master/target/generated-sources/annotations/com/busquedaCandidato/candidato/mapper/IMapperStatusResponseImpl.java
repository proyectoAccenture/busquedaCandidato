package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.StateResponseDto;
import com.busquedaCandidato.candidato.entity.StateEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-25T18:37:07-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class IMapperStatusResponseImpl implements IMapperStatusResponse {

    @Override
    public StateResponseDto StatusToStatusResponse(StateEntity stateEntity) {
        if ( stateEntity == null ) {
            return null;
        }

        String name = null;
        Long id = null;

        name = stateEntity.getName();
        id = stateEntity.getId();

        StateResponseDto stateResponseDto = new StateResponseDto( id, name );

        return stateResponseDto;
    }
}
