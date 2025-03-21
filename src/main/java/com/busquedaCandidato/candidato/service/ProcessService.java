package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.ProcessRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.dto.response.ProcessResponseDto;
import com.busquedaCandidato.candidato.entity.*;
import com.busquedaCandidato.candidato.exception.type.*;
import com.busquedaCandidato.candidato.mapper.IMapperProcessResponse;
import com.busquedaCandidato.candidato.repository.*;
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

        List<ProcessEntity> processes = processRepository.searchByCandidateNameOrLastName(query);
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
}
