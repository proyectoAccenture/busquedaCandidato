package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.ProcessRequestDto;
import com.candidateSearch.searching.dto.response.ProcessResponseDto;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.exception.type.CandidateBlockedException;
import com.candidateSearch.searching.exception.type.CannotBeCreateException;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.exception.type.ItAlreadyProcessWithIdPostulation;
import com.candidateSearch.searching.exception.type.PostulationIsOffException;
import com.candidateSearch.searching.exception.type.ResourceNotFoundException;
import com.candidateSearch.searching.exception.type.BadRequestException;
import com.candidateSearch.searching.exception.type.RoleIdNoExistException;
import com.candidateSearch.searching.exception.type.CandidateNoExistException;
import com.candidateSearch.searching.exception.type.CandidateNoPostulationException;
import com.candidateSearch.searching.mapper.IMapperProcess;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
import com.candidateSearch.searching.repository.IPostulationRepository;
import com.candidateSearch.searching.repository.IProcessRepository;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IRoleRepository;
import com.candidateSearch.searching.entity.utility.Status;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProcessService {
    private final IProcessRepository processRepository;
    private final ICandidateRepository candidateRepository;
    private final IPostulationRepository postulationRepository;
    private final IRoleRepository roleIDRepository;
    private final IMapperProcess mapperProcess;
    private final ICandidateStateRepository candidateStateRepository;


    public List<ProcessResponseDto> getProcessOfCandidateByRole(String roleName) {

        RoleEntity role = roleIDRepository.findByNameRole(roleName)
                .orElseThrow(RoleIdNoExistException::new);

        List<PostulationEntity> postulations = postulationRepository.findByRole(role);

        List<ProcessEntity> processes = postulations.stream()
                .map(PostulationEntity::getProcess)
                .filter(Objects::nonNull)
                .toList();

        return processes.stream()
                .map(mapperProcess::toDto)
                .collect(Collectors.toList());

    }

    public ProcessResponseDto getProcessByIdCandidate(Long id){

        Boolean postulationEntity = postulationRepository.existsByCandidateId(id);

        if(!postulationEntity){
            throw new EntityNoExistException();
        }

        CandidateEntity candidateEntity = candidateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        return processRepository.findById(candidateEntity.getId())
                .map(mapperProcess::toDto)
                .orElseThrow(EntityNoExistException::new);
    }

    public ProcessResponseDto getByIdProcess(Long id){
        ProcessEntity processEntity = processRepository.findById(id)
                .orElseThrow(CandidateNoExistException::new);

        List<CandidateStateEntity> filteredStates = processEntity.getCandidateState().stream()
                .filter(cs -> cs.getStatusHistory() == Status.ACTIVE)
                .toList();

        processEntity.setCandidateState(filteredStates);

        return mapperProcess.toDto(processEntity);
    }

    public List<ProcessResponseDto> getAllProcess(){
        return processRepository.findAll().stream()
                .filter(process -> process.getStatus() == Status.ACTIVE)
                .map(mapperProcess::toDto)
                .collect(Collectors.toList());
    }

    public List<ProcessResponseDto> getProcessByPostulationId(Long postulationId) {
        validateLongId(postulationId);

        Optional<ProcessEntity> processes = processRepository.findByPostulationId(postulationId);
        validateListProcess(processes);

        return processes.stream()
                .map(mapperProcess::toDto).collect(Collectors.toList());
    }

    public List<ProcessResponseDto> getSearchProcessesByCandidateFullName(String query) {
        validateStringQuery(query);
        query = normalizeQuery(query);

        String[] words = query.split(" ");

        String word1 = words.length > 0 ? words[0] : null;
        String word2 = words.length > 1 ? words[1] : null;
        String word3 = words.length > 2 ? words[2] : null;
        String word4 = words.length > 3 ? words[3] : null;

        List<ProcessEntity> processes = processRepository.searchByCandidateNameOrLastName2(word1, word2, word3, word4);

        validateListProcess(processes);

        return processes.stream()
                .map(mapperProcess::toDto)
                .collect(Collectors.toList());
    }

    public ProcessResponseDto saveProcess(ProcessRequestDto processRequestDto) {

        if(processRequestDto.getStatus().equals(Status.INACTIVE)){
            throw new CannotBeCreateException();
        }

        if(processRequestDto.getStatus().equals(Status.BLOCKED)){
            throw new CandidateBlockedException();
        }
        PostulationEntity postulationEntity = postulationRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(CandidateNoPostulationException::new);

        if (postulationEntity.getStatus().equals(Status.INACTIVE)) {
            throw new PostulationIsOffException();
        }

        if (postulationEntity.getProcess() != null) {
            ProcessEntity existingProcess = postulationEntity.getProcess();

            if (existingProcess.getStatus().equals(Status.ACTIVE)) {
                throw new ItAlreadyProcessWithIdPostulation();
            }
        }

        ProcessEntity process = new ProcessEntity();
        process.setDescription(processRequestDto.getDescription());
        process.setAssignmentDate(processRequestDto.getAssignedDate());
        process.setStatus(processRequestDto.getStatus());
        process.setPostulation(postulationEntity);

        ProcessEntity processEntitySave = processRepository.save(process);

        postulationEntity.setProcess(processEntitySave);
        postulationRepository.save(postulationEntity);

        return mapperProcess.toDto(processEntitySave);
    }

    public Optional<ProcessResponseDto> updateProcess(Long id, ProcessRequestDto processRequestDto) {

        if(processRequestDto.getStatus().equals(Status.BLOCKED)){
            throw new CannotBeCreateException();
        }

        ProcessEntity existingEntity  = processRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        PostulationEntity postulation = postulationRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(CandidateNoPostulationException::new);

        if (processRequestDto.getStatus() == Status.ACTIVE) {
            ProcessEntity currentActiveProcess = postulation.getProcess();

            if (!existingEntity.equals(currentActiveProcess) && currentActiveProcess != null && currentActiveProcess.getStatus() == Status.ACTIVE) {
                throw new ItAlreadyProcessWithIdPostulation();
            }
        }

        existingEntity.setPostulation(postulation);
        existingEntity.setDescription(processRequestDto.getDescription());
        existingEntity.setAssignmentDate(processRequestDto.getAssignedDate());
        existingEntity.setStatus(processRequestDto.getStatus());

        ProcessEntity processSaved = processRepository.save(existingEntity);

        postulation.setProcess(processSaved);
        postulationRepository.save(postulation);

        return Optional.of(mapperProcess.toDto(processSaved));
    }

    @Transactional
    public void deleteProcess(Long id){
        ProcessEntity existingProcess = processRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingProcess.setStatus(Status.INACTIVE);

        List<CandidateStateEntity> candidateStateFind = candidateStateRepository.findByProcessId(existingProcess.getId());
        if (candidateStateFind != null) {
            for (CandidateStateEntity candidateState : candidateStateFind) {
                if (candidateState.getStatusHistory().equals(Status.ACTIVE)) {
                    candidateState.setStatusHistory(Status.INACTIVE);
                    candidateStateRepository.save(candidateState);
                }
            }
        }

        processRepository.save(existingProcess);
    }

    private void validateLongId(Long id){
        if (id == null || id <= 0) {
            throw new BadRequestException("The application ID must be a positive number.");
        }

        if (!postulationRepository.existsById(id)) {
            throw new ResourceNotFoundException("No postulation found with ID: " + id);
        }
    }

    private void validateListProcess(List<ProcessEntity> processEntities) {
        if (processEntities.isEmpty()) {
            throw new ResourceNotFoundException("No processes found for the given search criteria.");
        }
    }

    private void validateListProcess(Optional<ProcessEntity> processEntities) {
        if (processEntities.isEmpty()) {
            throw new ResourceNotFoundException("No processes found for the given search criteria.");
        }
    }

    private void validateStringQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new BadRequestException("The search query cannot be empty.");
        }
    }

    private String normalizeQuery(String query) {
        if (query == null || query.isBlank()) {
            return query;
        }
        query = query.trim();
        return query.replaceAll("[áÁ]", "a")
                .replaceAll("[éÉ]", "e")
                .replaceAll("[íÍ]", "i")
                .replaceAll("[óÓ]", "o")
                .replaceAll("[úÚ]", "u");
    }
}

