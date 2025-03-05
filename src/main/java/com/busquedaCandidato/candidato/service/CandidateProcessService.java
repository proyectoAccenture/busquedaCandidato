package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateProcessRequestDto;
import com.busquedaCandidato.candidato.dto.request.CandidateProcessRequestUpdateDto;
import com.busquedaCandidato.candidato.dto.response.CandidateProcessResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateProcessEntity;
import com.busquedaCandidato.candidato.entity.PhaseEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.exception.type.*;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateProcessResponse;
import com.busquedaCandidato.candidato.repository.IPhaseRepository;
import com.busquedaCandidato.candidato.repository.ICandidateProcessRepository;
import com.busquedaCandidato.candidato.repository.IProcessRepository;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateProcessService {

    private final IProcessRepository processRepository;
    private final IPhaseRepository phaseRepository;
    private final IStateRepository stateRepository;
    private final ICandidateProcessRepository candidateProcessRepository;
    private final IMapperCandidateProcessResponse mapperHistoryProcessResponse;

    public CandidateProcessResponseDto addPhaseToProcess(CandidateProcessRequestDto candidateProcessRequestDto){

        ProcessEntity processEntity = processRepository.findById(candidateProcessRequestDto.getProcessId())
                .orElseThrow(ProcessNoExistException::new);

        Optional<CandidateProcessEntity> currentPhaseOptional = candidateProcessRepository.findTopByProcessOrderByAssignedDateDesc(processEntity);

        if (currentPhaseOptional.isPresent()) {
            CandidateProcessEntity lastCandidateProcess = currentPhaseOptional.get();
            System.out.println("Última fase encontrada con status: " + lastCandidateProcess.getStatus());

            if (!lastCandidateProcess.getStatus()) {
                throw new CannotBeCreateCandidateProcessException();
            }
        }

        PhaseEntity phaseEntity = phaseRepository.findById(candidateProcessRequestDto.getPhaseId())
                .orElseThrow(PhaseNoFoundException::new);

        StateEntity stateEntity = stateRepository.findById(candidateProcessRequestDto.getStateId())
                .orElseThrow(StateNoFoundException::new);

        CandidateProcessEntity newCandidateProcess = new CandidateProcessEntity();
        newCandidateProcess.setProcess(processEntity);
        newCandidateProcess.setPhase(phaseEntity);
        newCandidateProcess.setState(stateEntity);
        newCandidateProcess.setDescription(candidateProcessRequestDto.getDescription());
        newCandidateProcess.setStatus(candidateProcessRequestDto.getStatus());
        newCandidateProcess.setAssignedDate(candidateProcessRequestDto.getAssignedDate());

        CandidateProcessEntity savedEntity = candidateProcessRepository.save(newCandidateProcess);
        return mapperHistoryProcessResponse.CandidateProcessToCandidateProcessResponse(savedEntity);
    }

    public CandidateProcessResponseDto getCandidateProcessById(Long processId){
        ProcessEntity processEntity = processRepository.findById(processId)
                .orElseThrow(ProcessNoExistException::new);

        CandidateProcessEntity currentPhase = candidateProcessRepository
                .findTopByProcessOrderByAssignedDateDesc(processEntity)
                .orElseThrow(NotPhasesAssignedException::new);

        return mapperHistoryProcessResponse.CandidateProcessToCandidateProcessResponse(currentPhase);
    }

    public List<CandidateProcessResponseDto> getAllCandidateProcess(){
        return candidateProcessRepository.findAll().stream()
                .map(mapperHistoryProcessResponse::CandidateProcessToCandidateProcessResponse)
                .collect(Collectors.toList());
    }

    public Optional<CandidateProcessResponseDto> updateCandidateProcess(Long id, CandidateProcessRequestUpdateDto candidateProcessRequestUpdateDto) {
        return candidateProcessRepository.findById(id)
                .map(existingEntity -> {

                    StateEntity newState = stateRepository.findById(candidateProcessRequestUpdateDto.getStateId())
                            .orElseThrow(StateNoFoundException::new);

                    existingEntity.setState(newState);
                    existingEntity.setDescription(candidateProcessRequestUpdateDto.getDescription());
                    existingEntity.setStatus(candidateProcessRequestUpdateDto.getStatus());
                    existingEntity.setAssignedDate(candidateProcessRequestUpdateDto.getAssignedDate());

                    CandidateProcessEntity updatedEntity = candidateProcessRepository.save(existingEntity);
                    return mapperHistoryProcessResponse.CandidateProcessToCandidateProcessResponse(updatedEntity);
                });
    }

    public boolean deleteCandidateProcess(Long id){
        if (candidateProcessRepository.existsById(id)) {
            candidateProcessRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
