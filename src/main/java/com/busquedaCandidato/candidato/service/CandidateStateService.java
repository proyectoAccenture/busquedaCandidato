package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateStateRequestDto;
import com.busquedaCandidato.candidato.dto.request.CandidateStateRequestUpdateDto;
import com.busquedaCandidato.candidato.dto.response.CandidateStateResponseDto;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.CandidateStateEntity;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.exception.type.ProcessNoExistException;
import com.busquedaCandidato.candidato.exception.type.StateNoFoundException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.exception.type.CannotBeCreateCandidateProcessException;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyHasRelationException;
import com.busquedaCandidato.candidato.exception.type.PostulationProcessException;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateStateResponse;
import com.busquedaCandidato.candidato.repository.ICandidateStateRepository;
import com.busquedaCandidato.candidato.repository.IProcessRepository;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateStateService {

    private final IProcessRepository processRepository;
    private final IStateRepository stateRepository;
    private final ICandidateStateRepository candidatePhasesRepository;
    private final IMapperCandidateStateResponse mapperCandidatePhasesResponse;

    public CandidateStateResponseDto addPhaseToProcess(CandidateStateRequestDto candidateStateRequestDto){

        ProcessEntity processEntity = processRepository.findById(candidateStateRequestDto.getProcessId())
                .orElseThrow(ProcessNoExistException::new);

        if (processEntity.getPostulation() == null || processEntity.getPostulation().getCandidate() == null) {
            throw new RuntimeException("Postulation within candidate associated");
        }
        CandidateEntity candidate = processEntity.getPostulation().getCandidate();
        PostulationEntity postulation = processEntity.getPostulation();

        if (candidatePhasesRepository.existsByCandidateAndPostulationAndProcess(candidate, postulation, processEntity)) {
            throw new PostulationProcessException("The candidate has process active in this postulation");
        }

        Optional<CandidateStateEntity> currentPhaseOptional = candidatePhasesRepository.findTopByProcessOrderByIdDesc(processEntity);

        if (currentPhaseOptional.isPresent()) {
            CandidateStateEntity lastCandidatePhases = currentPhaseOptional.get();

            if (!lastCandidatePhases.getStatus()) {
                throw new CannotBeCreateCandidateProcessException();
            }
        }

        StateEntity stateEntity = stateRepository.findById(candidateStateRequestDto.getStateId())
                .orElseThrow(StateNoFoundException::new);

        CandidateStateEntity newCandidateProcess = new CandidateStateEntity();
        newCandidateProcess.setProcess(processEntity);
        newCandidateProcess.setState(stateEntity);
        newCandidateProcess.setDescription(candidateStateRequestDto.getDescription());
        newCandidateProcess.setStatus(candidateStateRequestDto.getStatus());
        newCandidateProcess.setAssignedDate(candidateStateRequestDto.getAssignedDate());

        CandidateStateEntity savedEntity = candidatePhasesRepository.save(newCandidateProcess);
        return mapperCandidatePhasesResponse.toDto(savedEntity);
    }

    public CandidateStateResponseDto getCandidatePhasesById(Long processId){
         CandidateStateEntity candidatePhases = candidatePhasesRepository.findById(processId)
                .orElseThrow(ProcessNoExistException::new);

        return mapperCandidatePhasesResponse.toDto(candidatePhases);
    }

    public List<CandidateStateResponseDto> getAllCandidatePhases(){
        return candidatePhasesRepository.findAll().stream()
                .map(mapperCandidatePhasesResponse::toDto)
                .collect(Collectors.toList());
    }

    public Optional<CandidateStateResponseDto> updateCandidatePhases(Long id, CandidateStateRequestUpdateDto candidateStateRequestUpdateDto) {
        CandidateStateEntity existingEntity  = candidatePhasesRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        StateEntity newState = stateRepository.findById(candidateStateRequestUpdateDto.getStateId())
                .orElseThrow(StateNoFoundException::new);

        existingEntity.setState(newState);
        existingEntity.setDescription(candidateStateRequestUpdateDto.getDescription());
        existingEntity.setStatus(candidateStateRequestUpdateDto.getStatus());
        existingEntity.setAssignedDate(candidateStateRequestUpdateDto.getAssignedDate());
        CandidateStateEntity updatedEntity = candidatePhasesRepository.save(existingEntity);

        return Optional.of(mapperCandidatePhasesResponse.toDto(updatedEntity));
    }

    public void deleteCandidatePhases(Long id){
        CandidateStateEntity existingCandidatePhase = candidatePhasesRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        if (processRepository.existsByCandidatePhasesId(id)) {
            throw new EntityAlreadyHasRelationException();
        }

        candidatePhasesRepository.delete(existingCandidatePhase);
    }
}
