package com.busquedaCandidato.candidato.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.busquedaCandidato.candidato.dto.request.PhaseRequestDto;
import com.busquedaCandidato.candidato.dto.response.PhaseResponseDto;
import com.busquedaCandidato.candidato.entity.PhaseEntity;
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

    public Optional<PhaseResponseDto> getPhase(Long id){
        return phaseRepository.findById(id)
                .map(mapperPhaseResponse:: PhaseToPhaseResponse );

    }

      public List<PhaseResponseDto> getAllPhase(){
        return phaseRepository.findAll().stream()
                .map(mapperPhaseResponse::PhaseToPhaseResponse)
                .collect(Collectors.toList());
    }

    public PhaseResponseDto savePhase(PhaseRequestDto phaseRequestDto) {
        if(phaseRepository.existsByName(phaseRequestDto.getName())){
            throw new EntityAlreadyExistsException();
        }
        PhaseEntity phaseEntity = mapperPhaseRequest.PhaseRequestToPhase(phaseRequestDto);
        PhaseEntity phaseEntitySave = phaseRepository.save(phaseEntity);
        return mapperPhaseResponse.PhaseToPhaseResponse(phaseEntitySave);
    }

     public Optional<PhaseResponseDto> updatePhase(Long id, PhaseRequestDto phaseRequestDto) {
        return phaseRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setName(phaseRequestDto.getName());
                    return mapperPhaseResponse.PhaseToPhaseResponse(phaseRepository.save(existingEntity));
                });
    }

    public boolean deletePhase(Long id){
        if (phaseRepository.existsById(id)) {
            phaseRepository.deleteById(id);
            return true;
        }
        return false;
    }




}
