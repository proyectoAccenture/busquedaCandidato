package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.exception.type.ItAlreadyProcessWithIdPostulation;
import com.busquedaCandidato.candidato.exception.type.PostulationIsOffException;
import com.busquedaCandidato.candidato.exception.type.ResourceNotFoundException;
import com.busquedaCandidato.candidato.exception.type.BadRequestException;
import com.busquedaCandidato.candidato.exception.type.RoleIdNoExistException;
import com.busquedaCandidato.candidato.exception.type.CandidateNoExistException;
import com.busquedaCandidato.candidato.exception.type.CandidateNoPostulationException;
import com.busquedaCandidato.candidato.mapper.IMapperProcessResponse;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
import com.busquedaCandidato.candidato.repository.IProcessRepository;
import com.busquedaCandidato.candidato.repository.IRoleIDRepository;
import com.busquedaCandidato.candidato.repository.IVacancyCompanyRepository;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
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
    private final IRoleIDRepository roleIDRepository;
    private final IVacancyCompanyRepository vacancyCompanyRepository;
    private final IMapperProcessResponse mapperProcessResponse;

    public List<ProcessResponseDto> getProcessOfCandidateByRole(String roleName) {

        RoleIDEntity roleOptional = roleIDRepository.findByName(roleName)
                .orElseThrow(RoleIdNoExistException::new);

        Long roleId = roleOptional.getId();

        List<VacancyCompanyEntity> vacancies = vacancyCompanyRepository.findByRoleId(roleId);

        List<Long> vacancyIds = vacancies.stream()
                .map(VacancyCompanyEntity::getId)
                .toList();

        List<PostulationEntity> postulations = postulationRepository.findByVacancyCompanyIdIn(vacancyIds);

        List<Long> processIds = postulations.stream()
                .map(PostulationEntity::getProcess)
                .filter(Objects::nonNull)
                .map(ProcessEntity::getId)
                .toList();

        List<ProcessEntity> processes = processRepository.findByIdIn(processIds);

        return processes.stream()
                .map(mapperProcessResponse::toDto)
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
                .map(mapperProcessResponse::toDto)
                .orElseThrow(EntityNoExistException::new);
     }

    public ProcessResponseDto getByIdProcess(Long id){
        return processRepository.findById(id)
                .map(mapperProcessResponse::toDto)
                .orElseThrow(CandidateNoExistException::new);
    }

    public List<ProcessResponseDto> getAllProcess(){
        return processRepository.findAll().stream()
                .map(mapperProcessResponse::toDto)
                .collect(Collectors.toList());
    }

    public List<ProcessResponseDto> getProcessByPostulationId(Long postulationId) {
        validateLongId(postulationId);

        Optional<ProcessEntity> processes = processRepository.findByPostulationId(postulationId);
        validateListProcess(processes);

        return processes.stream()
                .map(mapperProcessResponse::toDto).collect(Collectors.toList());
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
                .map(mapperProcessResponse::toDto)
                .collect(Collectors.toList());
    }

    public ProcessResponseDto saveProcess(ProcessRequestDto processRequestDto) {

        Optional<ProcessEntity> existingProcess = processRepository.findByPostulationId(processRequestDto.getPostulationId());

        if (existingProcess.isPresent()) {
            throw new ItAlreadyProcessWithIdPostulation();
        }

        PostulationEntity postulationEntity = postulationRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(CandidateNoPostulationException::new);

        if(!postulationEntity.getStatus()){
            throw new PostulationIsOffException();
        }

        ProcessEntity newCandidateStatusHistory = new ProcessEntity();
        newCandidateStatusHistory.setDescription(processRequestDto.getDescription());
        newCandidateStatusHistory.setAssignmentDate(processRequestDto.getAssignedDate());
        newCandidateStatusHistory.setPostulation(postulationEntity);

        ProcessEntity processEntitySave = processRepository.save(newCandidateStatusHistory);
        return mapperProcessResponse.toDto(processEntitySave);
    }

    public Optional<ProcessResponseDto> updateProcess(Long id, ProcessRequestDto processRequestDto) {
        ProcessEntity existingEntity  = processRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        PostulationEntity postulation = postulationRepository.findById(processRequestDto.getPostulationId())
                .orElseThrow(CandidateNoPostulationException::new);

        existingEntity.setPostulation(postulation);
        existingEntity.setDescription(processRequestDto.getDescription());
        existingEntity.setAssignmentDate(processRequestDto.getAssignedDate());

        return Optional.of(mapperProcessResponse.toDto(processRepository.save(existingEntity)));
    }

    public void deleteProcess(Long id){
        PostulationEntity existingPostulation = postulationRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        postulationRepository.delete(existingPostulation);
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

