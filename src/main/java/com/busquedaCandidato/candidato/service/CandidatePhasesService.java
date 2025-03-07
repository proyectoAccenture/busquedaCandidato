package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidatePhasesRequestDto;
import com.busquedaCandidato.candidato.dto.request.CandidatePhasesRequestUpdateDto;
import com.busquedaCandidato.candidato.dto.response.CandidatePhasesResponseDto;
import com.busquedaCandidato.candidato.entity.CandidatePhasesEntity;
import com.busquedaCandidato.candidato.entity.PhaseEntity;
import com.busquedaCandidato.candidato.entity.CandidateStatusHistoryEntity;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.exception.type.*;
import com.busquedaCandidato.candidato.mapper.IMapperCandidatePhasesResponse;
import com.busquedaCandidato.candidato.repository.IPhaseRepository;
import com.busquedaCandidato.candidato.repository.ICandidatePhasesRepository;
import com.busquedaCandidato.candidato.repository.ICandidateStatusHistoryRepository;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatePhasesService {

    private final ICandidateStatusHistoryRepository processRepository;
    private final IPhaseRepository phaseRepository;
    private final IStateRepository stateRepository;
    private final ICandidatePhasesRepository candidatePhasesRepository;
    private final IMapperCandidatePhasesResponse mapperCandidatePhasesResponse;

    public CandidatePhasesResponseDto addPhaseToProcess(CandidatePhasesRequestDto candidatePhasesRequestDto){

       /*
        candidateStatusHistoryEntity_candidateStatusHistoryEntity = processRepository.findById(candidatePhasesRequestDto.getProcessId())
        //  .orElseThrow(ProcessNoExistException::new);

        //Optional<CandidatePhasesEntity> currentPhaseOptional = candidateProcessRepository.findTopByProcessOrderByAssignedDateDesc(candidateStatusHistoryEntity);

        if (currentPhaseOptional.isPresent()) {
            CandidatePhasesEntity lastCandidateProcess = currentPhaseOptional.get();
            System.out.println("Última fase encontrada con status: " + lastCandidateProcess.getStatus());

            if (!lastCandidateProcess.getStatus()) {
                throw new CannotBeCreateCandidateProcessException();
            }
        }
       */

        PhaseEntity phaseEntity = phaseRepository.findById(candidatePhasesRequestDto.getPhaseId())
                .orElseThrow(PhaseNoFoundException::new);

        StateEntity stateEntity = stateRepository.findById(candidatePhasesRequestDto.getStateId())
                .orElseThrow(StateNoFoundException::new);

        CandidatePhasesEntity newCandidateProcess = new CandidatePhasesEntity();
        newCandidateProcess.setPhase(phaseEntity);
        newCandidateProcess.setState(stateEntity);
        newCandidateProcess.setDescription(candidatePhasesRequestDto.getDescription());
        newCandidateProcess.setStatus(candidatePhasesRequestDto.getStatus());
        newCandidateProcess.setAssignedDate(candidatePhasesRequestDto.getAssignedDate());

        CandidatePhasesEntity savedEntity = candidatePhasesRepository.save(newCandidateProcess);
        return mapperCandidatePhasesResponse.CandidatePhasesToCandidatePhasesResponse(savedEntity);
    }

    public CandidatePhasesResponseDto getCandidatePhasesById(Long processId){
         CandidatePhasesEntity candidatePhases = candidatePhasesRepository.findById(processId)
                .orElseThrow(ProcessNoExistException::new);

        return mapperCandidatePhasesResponse.CandidatePhasesToCandidatePhasesResponse(candidatePhases);
    }


    public List<CandidatePhasesResponseDto> getAllCandidatePhases(){
        return candidatePhasesRepository.findAll().stream()
                .map(mapperCandidatePhasesResponse::CandidatePhasesToCandidatePhasesResponse)
                .collect(Collectors.toList());
    }

    public Optional<CandidatePhasesResponseDto> updateCandidatePhases(Long id, CandidatePhasesRequestUpdateDto candidatePhasesRequestUpdateDto) {
        return candidatePhasesRepository.findById(id)
                .map(existingEntity -> {

                    StateEntity newState = stateRepository.findById(candidatePhasesRequestUpdateDto.getStateId())
                            .orElseThrow(StateNoFoundException::new);

                    existingEntity.setState(newState);
                    existingEntity.setDescription(candidatePhasesRequestUpdateDto.getDescription());
                    existingEntity.setStatus(candidatePhasesRequestUpdateDto.getStatus());
                    existingEntity.setAssignedDate(candidatePhasesRequestUpdateDto.getAssignedDate());

                    CandidatePhasesEntity updatedEntity = candidatePhasesRepository.save(existingEntity);
                    return mapperCandidatePhasesResponse.CandidatePhasesToCandidatePhasesResponse(updatedEntity);
                });
    }

    public boolean deleteCandidatePhases(Long id){
        if (candidatePhasesRepository.existsById(id)) {
            candidatePhasesRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
