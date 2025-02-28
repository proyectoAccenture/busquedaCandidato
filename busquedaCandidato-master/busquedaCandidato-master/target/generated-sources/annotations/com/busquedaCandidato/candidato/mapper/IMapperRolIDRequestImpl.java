package com.busquedaCandidato.candidato.mapper;

import com.busquedaCandidato.candidato.dto.request.RolIDRequestDto;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-26T16:24:59-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class IMapperRolIDRequestImpl implements IMapperRolIDRequest {

    @Override
    public RoleIDEntity RolIDRequestToStatus(RolIDRequestDto rolIDRequestDto) {
        if ( rolIDRequestDto == null ) {
            return null;
        }

        RoleIDEntity roleIDEntity = new RoleIDEntity();

        roleIDEntity.setName( rolIDRequestDto.getName() );

        return roleIDEntity;
    }
}
