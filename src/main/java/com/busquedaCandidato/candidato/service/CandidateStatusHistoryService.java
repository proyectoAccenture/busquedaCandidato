package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateStatusHistoryRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateStatusHistoryResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.CandidatePhasesEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.CandidateStatusHistoryEntity;
import com.busquedaCandidato.candidato.exception.type.CandidateNoPostulation;
import com.busquedaCandidato.candidato.exception.type.StateNoFoundException;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateStatusHistoryResponse;
import com.busquedaCandidato.candidato.repository.ICandidatePhasesRepository;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
import com.busquedaCandidato.candidato.repository.ICandidateStatusHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CandidateStatusHistoryService {

    private final ICandidateStatusHistoryRepository processRepository;
    //private final ICandidateRepository candidateRepository;
    private final IPostulationRepository postulationRepository;
    private final ICandidatePhasesRepository candidatePhasesRepository;
    private final IMapperCandidateStatusHistoryResponse mapperProcessResponse;

    public Optional<CandidateStatusHistoryResponseDto> getByIdCandidateStatusHistory(Long id){
        return processRepository.findById(id)
                .map(mapperProcessResponse::CandidateStatusHistoryToResponse);
    }

    public List<CandidateStatusHistoryResponseDto> getAllCandidateStatusHistory(){
        return processRepository.findAll().stream()
                .map(mapperProcessResponse::CandidateStatusHistoryToResponse)
                .collect(Collectors.toList());
    }

    public CandidateStatusHistoryResponseDto saveCandidateStatusHistory(CandidateStatusHistoryRequestDto candidateStatusHistoryRequestDto) {

        Optional<PostulationEntity> postulationEntityOptional = postulationRepository.findById(candidateStatusHistoryRequestDto.getPostulationId());

        if(postulationEntityOptional.isEmpty()){
            throw new CandidateNoPostulation();
        }

        CandidatePhasesEntity candidatePhases = candidatePhasesRepository.findById(candidateStatusHistoryRequestDto.getCandidatePhasesId())
                .orElseThrow(EntityNotFoundException::new);

        CandidateStatusHistoryEntity candidateStatusHistoryEntity = new CandidateStatusHistoryEntity();
        candidateStatusHistoryEntity.setCandidatePhases(candidatePhases);

        PostulationEntity postulation = postulationRepository.findById(candidateStatusHistoryRequestDto.getPostulationId())
                .orElseThrow(EntityNotFoundException::new);

        CandidateStatusHistoryEntity  newCandidateStatusHistory = new CandidateStatusHistoryEntity();
        newCandidateStatusHistory.setDescription(candidateStatusHistoryEntity.getDescription());
        newCandidateStatusHistory.setAssignmentDate(candidateStatusHistoryEntity.getAssignmentDate());
        newCandidateStatusHistory.setPostulation(postulation);
        newCandidateStatusHistory.setCandidatePhases(candidatePhases);


        CandidateStatusHistoryEntity candidateStatusHistoryEntitySave = processRepository.save(newCandidateStatusHistory);

        return mapperProcessResponse.CandidateStatusHistoryToResponse(candidateStatusHistoryEntitySave);
    }

    public Optional<CandidateStatusHistoryResponseDto> updateCandidateStatusHistory(Long id, CandidateStatusHistoryRequestDto candidateStatusHistoryRequestDto) {
        return processRepository.findById(id)
                .map(existingEntity -> {

                    PostulationEntity postulation = postulationRepository.findById(candidateStatusHistoryRequestDto.getPostulationId())
                            .orElseThrow(StateNoFoundException::new);

                    CandidatePhasesEntity candidatePhases = candidatePhasesRepository.findById(candidateStatusHistoryRequestDto.getCandidatePhasesId())
                                    .orElseThrow(StateNoFoundException::new);


                    existingEntity.setPostulation(postulation);
                    existingEntity.setCandidatePhases(candidatePhases);
                    existingEntity.setDescription(candidateStatusHistoryRequestDto.getDescription());
                    return mapperProcessResponse.CandidateStatusHistoryToResponse(processRepository.save(existingEntity));
                });
    }

    public boolean deleteCandidateStatusHistory(Long id){
        if (processRepository.existsById(id)) {
            processRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
