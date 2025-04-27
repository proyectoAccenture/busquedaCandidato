package com.busquedaCandidato.candidato.service;

import java.util.List;
import java.util.stream.Collectors;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import org.springframework.stereotype.Service;
import com.busquedaCandidato.candidato.dto.request.OriginRequestDto;
import com.busquedaCandidato.candidato.dto.response.OriginResponseDto;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.mapper.IMapperOrigin;
import com.busquedaCandidato.candidato.repository.IOriginRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OriginService {

    private final IOriginRepository originRepository;
    private final IMapperOrigin mapperOrigin;

    public OriginResponseDto getOriginById(Long id){
        return originRepository.findById(id)
                .map(mapperOrigin::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<OriginResponseDto> getAllOrigin(){
        return originRepository.findAll().stream()
                .map(mapperOrigin::toDto)
                .collect(Collectors.toList());
    }

    public OriginResponseDto saveOrigin(OriginRequestDto originRequestDto) {
        if(originRepository.existsByName(originRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }

        OriginEntity originEntity = mapperOrigin.toEntity(originRequestDto);
        OriginEntity originEntitySave = originRepository.save(originEntity);

        return mapperOrigin.toDto(originEntitySave);
    }

    public OriginResponseDto updateOrigin(Long id, OriginRequestDto originRequestDto) {
        OriginEntity existingOrigin = originRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingOrigin.setName(originRequestDto.getName());
        OriginEntity updatedOrigin = originRepository.save(existingOrigin);

        return mapperOrigin.toDto(updatedOrigin);
    }

    public void deleteOrigin(Long id){
        OriginEntity existingOrigin = originRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        originRepository.delete(existingOrigin);
    }
}
