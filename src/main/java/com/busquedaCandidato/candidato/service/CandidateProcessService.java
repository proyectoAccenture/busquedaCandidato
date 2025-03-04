package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateProcessRequestDto;
import com.busquedaCandidato.candidato.dto.request.CandidateProcessRequestUpdateDto;
import com.busquedaCandidato.candidato.dto.response.CandidateProcessResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateProcessEntity;
import com.busquedaCandidato.candidato.entity.PhaseEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.exception.type.*;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateProcessRequest;
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
    private final ICandidateProcessRepository historyProcessRepository;
    private final IMapperCandidateProcessResponse mapperHistoryProcessResponse;

    public CandidateProcessResponseDto addPhaseToProcess(CandidateProcessRequestDto historyProcessRequestDTO){

        ProcessEntity process = processRepository.findById(historyProcessRequestDTO.getProcessId())
                .orElseThrow(ProcessNoExistException::new);

        PhaseEntity phase = phaseRepository.findById(historyProcessRequestDTO.getPhaseId())
                .orElseThrow(StateNoFoundException::new);

        StateEntity state = stateRepository.findById(historyProcessRequestDTO.getStateId())
                .orElseThrow(PhaseNoFoundException::new);

        CandidateProcessEntity newCandidateProcess = new CandidateProcessEntity();
        newCandidateProcess.setProcess(process);
        newCandidateProcess.setPhase(phase);
        newCandidateProcess.setState(state);
        newCandidateProcess.getStatus();
        newCandidateProcess.setAssignedDate(historyProcessRequestDTO.getAssignedDate());

        CandidateProcessEntity savedEntity = historyProcessRepository.save(newCandidateProcess);
        return mapperHistoryProcessResponse.CandidateProcessToCandidateProcessResponse(savedEntity);
    }

    public CandidateProcessResponseDto getCandidateProcessById(Long processId){
        CandidateProcessEntity currentPhase = historyProcessRepository.findTopByProcessIdOrderByAssignedDateDesc(processId)
                .orElseThrow(NotPhasesAssignedException::new);

        return mapperHistoryProcessResponse.CandidateProcessToCandidateProcessResponse(currentPhase);
    }

    public List<CandidateProcessResponseDto> getAllCandidateProcess(){
        return historyProcessRepository.findAll().stream()
                .map(mapperHistoryProcessResponse::CandidateProcessToCandidateProcessResponse)
                .collect(Collectors.toList());
    }

    public Optional<CandidateProcessResponseDto> updateCandidateProcess(Long id, CandidateProcessRequestUpdateDto candidateProcessRequestUpdateDto) {
        return historyProcessRepository.findById(id)
                .map(existingEntity -> {

                    StateEntity newState = stateRepository.findById(candidateProcessRequestUpdateDto.getStateId())
                            .orElseThrow(StateNoFoundException::new);

                    existingEntity.setState(newState);
                    existingEntity.setDescription(candidateProcessRequestUpdateDto.getDescription());
                    existingEntity.setStatus(candidateProcessRequestUpdateDto.getStatus());
                    existingEntity.setAssignedDate(candidateProcessRequestUpdateDto.getAssignedDate());

                    CandidateProcessEntity updatedEntity = historyProcessRepository.save(existingEntity);

                    return mapperHistoryProcessResponse.CandidateProcessToCandidateProcessResponse(updatedEntity);
                });
    }

    public boolean deleteCandidateProcess(Long id){
        if (historyProcessRepository.existsById(id)) {
            historyProcessRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
