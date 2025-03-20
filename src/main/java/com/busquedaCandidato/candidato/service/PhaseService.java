package com.busquedaCandidato.candidato.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.busquedaCandidato.candidato.entity.PhaseEntity;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import org.springframework.stereotype.Service;
import com.busquedaCandidato.candidato.dto.request.PhaseRequestDto;
import com.busquedaCandidato.candidato.dto.response.PhaseResponseDto;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.mapper.IMapperPhaseRequest;
import com.busquedaCandidato.candidato.mapper.IMapperPhaseResponse;
import com.busquedaCandidato.candidato.repository.IPhaseRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PhaseService {

    private final IPhaseRepository phaseRepository;
    private final IMapperPhaseResponse mapperPhaseResponse;
    private final IMapperPhaseRequest mapperPhaseRequest;

    public PhaseResponseDto getPhase(Long id){
        return phaseRepository.findById(id)
                .map(mapperPhaseResponse::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public List<PhaseResponseDto> getAllPhase(){
        return phaseRepository.findAll().stream()
                .map(mapperPhaseResponse::toDto)
                .collect(Collectors.toList());
    }

    public PhaseResponseDto savePhase(PhaseRequestDto phaseRequestDto) {
        if(phaseRepository.existsByName(phaseRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }

        PhaseEntity phaseEntity = mapperPhaseRequest.toEntity(phaseRequestDto);
        PhaseEntity phaseEntitySave = phaseRepository.save(phaseEntity);

        return mapperPhaseResponse.toDto(phaseEntitySave);
    }

    public PhaseResponseDto updatePhase(Long id, PhaseRequestDto phaseRequestDto) {
         PhaseEntity existingPhase = phaseRepository.findById(id)
                 .orElseThrow(EntityNoExistException::new);

         existingPhase.setName(phaseRequestDto.getName());
         PhaseEntity updatedPhase = phaseRepository.save(existingPhase);

         return mapperPhaseResponse.toDto(updatedPhase);
    }

    public void deletePhase(Long id){
        PhaseEntity existingPhase = phaseRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        phaseRepository.delete(existingPhase);
    }
}
