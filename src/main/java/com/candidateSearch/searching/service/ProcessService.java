package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.ProcessRequestDto;
import com.candidateSearch.searching.dto.request.validation.validator.PostulationValidator;
import com.candidateSearch.searching.dto.request.validation.validator.ProcessValidator;
import com.candidateSearch.searching.dto.response.PaginationResponseDto;
import com.candidateSearch.searching.dto.response.ProcessResponseDto;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.exception.type.CustomBadRequestException;
import com.candidateSearch.searching.exception.type.CustomConflictException;
import com.candidateSearch.searching.exception.type.CustomNotFoundException;
import com.candidateSearch.searching.mapper.IMapperProcess;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
import com.candidateSearch.searching.repository.IPostulationRepository;
import com.candidateSearch.searching.repository.IProcessRepository;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IRoleRepository;
import com.candidateSearch.searching.entity.utility.Status;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                .orElseThrow(()->new CustomNotFoundException("There is no role with that ID."));

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
            throw new CustomNotFoundException("There is no postulation with that ID.");
        }

        CandidateEntity candidateEntity = candidateRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no candidate with that ID."));

        return processRepository.findById(candidateEntity.getId())
                .map(mapperProcess::toDto)
                .orElseThrow(()-> new CustomNotFoundException("There is no process with that candidate ID."));
    }

    public ProcessResponseDto getByIdProcess(Long id){
        ProcessEntity processEntity = processRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no process with that ID."));

        if(processEntity.getStatus().equals(Status.ACTIVE)){
            List<CandidateStateEntity> filteredStates = processEntity.getCandidateState().stream()
                    .filter(cs -> cs.getStatusHistory() == Status.ACTIVE)
                    .toList();
            processEntity.setCandidateState(filteredStates);
        }

        if(processEntity.getStatus().equals(Status.INACTIVE)){
            List<CandidateStateEntity> filteredStates = processEntity.getCandidateState().stream()
                    .filter(cs -> cs.getStatusHistory() == Status.INACTIVE)
                    .toList();
            processEntity.setCandidateState(filteredStates);
        }

        if(processEntity.getStatus().equals(Status.BLOCKED)){
            List<CandidateStateEntity> filteredStates = processEntity.getCandidateState().stream()
                    .filter(cs -> cs.getStatusHistory() == Status.BLOCKED)
                    .toList();
            processEntity.setCandidateState(filteredStates);
        }
        return mapperProcess.toDto(processEntity);
    }

    public List<ProcessResponseDto> getAllProcess(){
        return processRepository.findAll().stream()
                .filter(process -> process.getStatus() == Status.ACTIVE)
                .map(mapperProcess::toDto)
                .collect(Collectors.toList());
    }
    public PaginationResponseDto<ProcessResponseDto> getAllProcessV2(List<Status> statuses,int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProcessEntity> processEntityPage =processRepository.findByStatusIn(
                PostulationValidator.validateStatusesOrDefault(statuses),
                pageable);

        List<ProcessResponseDto> responseDtoList =processEntityPage.getContent()
                .stream()
                .map(mapperProcess::toDto)
                .toList();

        return  new PaginationResponseDto<>(
                responseDtoList,
                processEntityPage.getNumber(),
                processEntityPage.getSize(),
                processEntityPage.getTotalPages(),
                processEntityPage.getTotalElements()
        );
    }

    public List<ProcessResponseDto> getProcessByPostulationId(Long postulationId) {
        ProcessValidator.validateLongId(postulationId,postulationRepository);
        Optional<ProcessEntity> processes = processRepository.findByPostulationId(postulationId);
        ProcessValidator.validateProcessOptionalNotEmpty(processes);

        return processes.stream()
                .map(mapperProcess::toDto).collect(Collectors.toList());
    }

    public List<ProcessResponseDto> getSearchProcessesByCandidateFullName(String query) {
        ProcessValidator.validateStringQueryNotEmpty(query);
        query = ProcessValidator.normalizeQueryNotEmpty(query);

        String[] words = query.split(" ");
        String word1 = words.length > 0 ? words[0] : null;
        String word2 = words.length > 1 ? words[1] : null;
        String word3 = words.length > 2 ? words[2] : null;
        String word4 = words.length > 3 ? words[3] : null;

        List<ProcessEntity> processes = processRepository.searchByCandidateNameOrLastName2(word1, word2, word3, word4);
        ProcessValidator.validateProcessListNotEmpty(processes);
        return processes.stream()
                .map(mapperProcess::toDto)
                .collect(Collectors.toList());
    }

    public PaginationResponseDto<ProcessResponseDto> getSearchProcessesByCandidateFullNameV2(String query, int page ,int size,List<Status>statuses){
        ProcessValidator.validateStringQueryNotEmpty(query);
        query = ProcessValidator.normalizeQueryNotEmpty(query);
        Pageable pageable = PageRequest.of(page, size);

        String[] words = query.split(" ");
        String word1 = words.length > 0 ? words[0] : null;
        String word2 = words.length > 1 ? words[1] : null;
        String word3 = words.length > 2 ? words[2] : null;
        String word4 = words.length > 3 ? words[3] : null;

        Page<ProcessEntity> processEntities=processRepository.searchByCandidateNameAndStatuses(
                word1,
                word2,
                word3,
                word4,
                ProcessValidator.validateStatusesOrDefault(statuses),
                pageable);

        List<ProcessResponseDto> responseDtoList =processEntities.getContent()
                .stream()
                .map(mapperProcess::toDto)
                .toList();

        return  new PaginationResponseDto<>(
                responseDtoList,
                processEntities.getNumber(),
                processEntities.getSize(),
                processEntities.getTotalPages(),
                processEntities.getTotalElements()
        );
    }

    public ProcessResponseDto saveProcess(ProcessRequestDto processRequestDto) {

        if(processRequestDto.getStatus().equals(Status.INACTIVE) || processRequestDto.getStatus().equals(Status.BLOCKED)){
            throw new CustomBadRequestException("Cannot be create or update, valid the status.");
        }

        PostulationEntity postulationEntity = postulationRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(()-> new CustomBadRequestException("The candidate must first have been nominated."));

        if (postulationEntity.getStatus().equals(Status.INACTIVE)) {
            throw new CustomConflictException("The postulation must be in true.");
        }

        if (postulationEntity.getProcess() != null) {
            ProcessEntity existingProcess = postulationEntity.getProcess();

            if (existingProcess.getStatus().equals(Status.ACTIVE)) {
                throw new CustomConflictException("There is already a process with that id postulation.");
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

        if (processRequestDto.getStatus() == Status.INACTIVE ||
                processRequestDto.getStatus() == Status.BLOCKED) {
            throw new CustomBadRequestException("Cannot be create or update, valid the status.");
        }

        ProcessEntity existingEntity  = processRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no process with that ID."));

        PostulationEntity postulation = postulationRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(()-> new CustomBadRequestException("The candidate must first have been nominated."));

        if (processRequestDto.getStatus() == Status.ACTIVE) {
            ProcessEntity currentActiveProcess = postulation.getProcess();

            if (!existingEntity.equals(currentActiveProcess) && currentActiveProcess != null && currentActiveProcess.getStatus() == Status.ACTIVE) {
                throw new CustomConflictException("You have already submitted an active application.");
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
    public void deleteProcess(Long id) {
        ProcessEntity existingProcess = processRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("There is no process with that ID."));

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
}

