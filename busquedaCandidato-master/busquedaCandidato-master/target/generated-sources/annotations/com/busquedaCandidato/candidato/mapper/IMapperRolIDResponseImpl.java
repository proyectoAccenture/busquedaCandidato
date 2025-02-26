package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.response.RolIDResponseDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-26T16:24:59-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class IMapperRolIDResponseImpl implements IMapperRolIDResponse {

    @Override
    public RolIDResponseDto RolIDToStatusResponse(RoleIDEntity roleIDEntity) {
        if ( roleIDEntity == null ) {
            return null;
        }

        String name = null;
        Long id = null;

        name = roleIDEntity.getName();
        id = roleIDEntity.getId();

        RolIDResponseDto rolIDResponseDto = new RolIDResponseDto( id, name );

        return rolIDResponseDto;
    }
}
