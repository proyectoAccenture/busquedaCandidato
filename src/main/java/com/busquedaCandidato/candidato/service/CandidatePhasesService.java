package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidatePhasesRequestDto;
import com.busquedaCandidato.candidato.dto.request.CandidatePhasesRequestUpdateDto;
import com.busquedaCandidato.candidato.dto.response.CandidatePhasesResponseDto;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.PhaseEntity;
import com.busquedaCandidato.candidato.entity.CandidatePhasesEntity;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.exception.type.ProcessNoExistException;
import com.busquedaCandidato.candidato.exception.type.StateNoFoundException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.exception.type.PhaseNoFoundException;
import com.busquedaCandidato.candidato.exception.type.CannotBeCreateCandidateProcessException;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyHasRelationException;
import com.busquedaCandidato.candidato.exception.type.PostulationProcessException;
import com.busquedaCandidato.candidato.mapper.IMapperCandidatePhasesResponse;
import com.busquedaCandidato.candidato.repository.IPhaseRepository;
import com.busquedaCandidato.candidato.repository.ICandidatePhasesRepository;
import com.busquedaCandidato.candidato.repository.IProcessRepository;
import com.busquedaCandidato.candidato.repository.IStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatePhasesService {

    private final IProcessRepository processRepository;
    private final IPhaseRepository phaseRepository;
    private final IStateRepository stateRepository;
    private final ICandidatePhasesRepository candidatePhasesRepository;
    private final IMapperCandidatePhasesResponse mapperCandidatePhasesResponse;

    public CandidatePhasesResponseDto addPhaseToProcess(CandidatePhasesRequestDto candidatePhasesRequestDto){

        ProcessEntity processEntity = processRepository.findById(candidatePhasesRequestDto.getProcessId())
                .orElseThrow(ProcessNoExistException::new);

        if (processEntity.getPostulation() == null || processEntity.getPostulation().getCandidate() == null) {
            throw new RuntimeException("Postulation within candidate associated");
        }
        CandidateEntity candidate = processEntity.getPostulation().getCandidate();
        PostulationEntity postulation = processEntity.getPostulation();

        if (candidatePhasesRepository.existsByCandidateAndPostulationAndProcess(candidate, postulation, processEntity)) {
            throw new PostulationProcessException("The candidate has process active in this postulation");
        }

        Optional<CandidatePhasesEntity> currentPhaseOptional = candidatePhasesRepository.findTopByProcessOrderByIdDesc(processEntity);

        if (currentPhaseOptional.isPresent()) {
            CandidatePhasesEntity lastCandidatePhases = currentPhaseOptional.get();

            if (!lastCandidatePhases.getStatus()) {
                throw new CannotBeCreateCandidateProcessException();
            }
        }

        PhaseEntity phaseEntity = phaseRepository.findById(candidatePhasesRequestDto.getPhaseId())
                .orElseThrow(PhaseNoFoundException::new);

        StateEntity stateEntity = stateRepository.findById(candidatePhasesRequestDto.getStateId())
                .orElseThrow(StateNoFoundException::new);

        CandidatePhasesEntity newCandidateProcess = new CandidatePhasesEntity();
        newCandidateProcess.setProcess(processEntity);
        newCandidateProcess.setPhase(phaseEntity);
        newCandidateProcess.setState(stateEntity);
        newCandidateProcess.setDescription(candidatePhasesRequestDto.getDescription());
        newCandidateProcess.setStatus(candidatePhasesRequestDto.getStatus());
        newCandidateProcess.setAssignedDate(candidatePhasesRequestDto.getAssignedDate());

        CandidatePhasesEntity savedEntity = candidatePhasesRepository.save(newCandidateProcess);
        return mapperCandidatePhasesResponse.toDto(savedEntity);
    }

    public CandidatePhasesResponseDto getCandidatePhasesById(Long processId){
         CandidatePhasesEntity candidatePhases = candidatePhasesRepository.findById(processId)
                .orElseThrow(ProcessNoExistException::new);

        return mapperCandidatePhasesResponse.toDto(candidatePhases);
    }

    public List<CandidatePhasesResponseDto> getAllCandidatePhases(){
        return candidatePhasesRepository.findAll().stream()
                .map(mapperCandidatePhasesResponse::toDto)
                .collect(Collectors.toList());
    }

    public Optional<CandidatePhasesResponseDto> updateCandidatePhases(Long id, CandidatePhasesRequestUpdateDto candidatePhasesRequestUpdateDto) {
        CandidatePhasesEntity existingEntity  = candidatePhasesRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        StateEntity newState = stateRepository.findById(candidatePhasesRequestUpdateDto.getStateId())
                .orElseThrow(StateNoFoundException::new);

        existingEntity.setState(newState);
        existingEntity.setDescription(candidatePhasesRequestUpdateDto.getDescription());
        existingEntity.setStatus(candidatePhasesRequestUpdateDto.getStatus());
        existingEntity.setAssignedDate(candidatePhasesRequestUpdateDto.getAssignedDate());
        CandidatePhasesEntity updatedEntity = candidatePhasesRepository.save(existingEntity);

        return Optional.of(mapperCandidatePhasesResponse.toDto(updatedEntity));
    }

    public void deleteCandidatePhases(Long id){
        CandidatePhasesEntity existingCandidatePhase = candidatePhasesRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        if (processRepository.existsByCandidatePhasesId(id)) {
            throw new EntityAlreadyHasRelationException();
        }

        candidatePhasesRepository.delete(existingCandidatePhase);
    }
}
