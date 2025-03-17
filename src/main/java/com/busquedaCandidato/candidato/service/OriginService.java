package com.busquedaCandidato.candidato.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import org.springframework.stereotype.Service;
import com.busquedaCandidato.candidato.dto.request.OriginRequestDto;
import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.mapper.IMapperOriginRequest;
import com.busquedaCandidato.candidato.mapper.IMapperOriginResponse;
import com.busquedaCandidato.candidato.repository.IOriginRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OriginService {

    private final IOriginRepository originRepository;
    private final IMapperOriginResponse mapperOriginResponse;
    private final IMapperOriginRequest mapperOriginRequest;

    public Optional<OriginResponseDto> getOrigin(Long id){
        return originRepository.findById(id)
                .map(mapperOriginResponse:: OriginToOriginResponse);

    }

    public List<OriginResponseDto> getAllOrigin(){
        return originRepository.findAll().stream()
                .map(mapperOriginResponse::OriginToOriginResponse)
                .collect(Collectors.toList());
    }

    public OriginResponseDto saveOrigin(OriginRequestDto originRequestDto) {
        if(originRepository.existsByName(originRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }
        OriginEntity originEntity = mapperOriginRequest.OriginRequestToOrigin(originRequestDto);
        OriginEntity originEntitySave = originRepository.save(originEntity);
        return mapperOriginResponse.OriginToOriginResponse(originEntitySave);
    }

    public Optional<OriginResponseDto> updateOrigin(Long id, OriginRequestDto originRequestDto) {
        return originRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setName(originRequestDto.getName());
                    return mapperOriginResponse.OriginToOriginResponse(originRepository.save(existingEntity));
                });
    }

    public boolean deleteOrigin(Long id){
        if (originRepository.existsById(id)) {
            originRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
