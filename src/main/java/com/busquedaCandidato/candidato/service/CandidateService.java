package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidatePhasesResponseDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponse;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;

import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import com.busquedaCandidato.candidato.exception.type.*;
import com.busquedaCandidato.candidato.mapper.IMapperCandidatePhasesResponse;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateRequest;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateResponse;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
import com.busquedaCandidato.candidato.repository.IRoleIDRepository;
import com.busquedaCandidato.candidato.repository.IVacancyCompanyRepository;
import com.busquedaCandidato.candidato.specification.CandidateSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final ICandidateRepository candidateRepository;
    private final IVacancyCompanyRepository vacancyCompanyRepository;
    private final IPostulationRepository postulationRepository;
    private final IRoleIDRepository roleIDRepository;
    private final IMapperCandidateResponse mapperCandidateResponse;
    private final IMapperCandidateRequest mapperCandidateRequest;
    private final IMapperCandidatePhasesResponse phasesMappers;

    public List<CandidateResponseDto> getCandidateByRole(String roleName) {

        RoleIDEntity roleOptional = roleIDRepository.findByName(roleName)
                .orElseThrow(RoleIdNoExistException::new);

        Long roleId = roleOptional.getId();

        List<VacancyCompanyEntity> vacancies = vacancyCompanyRepository.findByRoleId(roleId);

        List<Long> vacancyIds = vacancies.stream()
                .map(VacancyCompanyEntity::getId)
                .toList();

        List<PostulationEntity> postulations = postulationRepository.findByVacancyCompanyIdIn(vacancyIds);

        List<Long> candidateIds = postulations.stream()
                .map(PostulationEntity::getCandidate)
                .filter(Objects::nonNull)
                .map(CandidateEntity::getId)
                .toList();

        List<CandidateEntity> candidates = candidateRepository.findByIdIn(candidateIds);

        return candidates.stream()
                .map(mapperCandidateResponse::toDto)
                .collect(Collectors.toList());
    }

    public CandidateResponse getSearchCandidates(String query, int page, int size) {
        validationQuery(query);
        Pageable pageable = PageRequest.of(page, size);

        Page<CandidateEntity> candidates = candidateRepository.searchCandidates(query, pageable);
        validationListPage(candidates);

        List<CandidateResponseDto> candidateDTOs = candidates.getContent().stream()
                .map(mapperCandidateResponse::toDto)
                .toList();

        return new CandidateResponse(
                candidateDTOs,
                candidates.getNumber(),
                candidates.getSize(),
                candidates.getTotalPages(),
                candidates.getTotalElements()
        );
    }

    public CandidateResponse getSearchCandidatesFullName(String query) {
        validationQuery(query);
        validationQueryNumber(query);
        Pageable pageable = PageRequest.of(0, 10);

        List<CandidateEntity> candidates = candidateRepository.searchByNameOrLastName(query, pageable);
        validationListCandidate(candidates);

        List<CandidateResponseDto> candidateDTOs = candidates.stream()
                .map(mapperCandidateResponse::toDto)
                .toList();

        return new CandidateResponse(
                candidateDTOs,
                0,
                candidateDTOs.size(),
                1,
                candidateDTOs.size()
        );
    }

    public List<CandidateResponseDto> getSearchCandidatesByNameOrRoleId(String searchValue) {
        validationQuery(searchValue);
        List<CandidateEntity> candidates = candidateRepository.findAll(CandidateSpecification.filterBySingleField(searchValue));
        validationListCandidate(candidates);

        return candidates.stream().map(candidate -> {
            List<CandidatePhasesResponseDto> phases = candidate.getPostulations().stream()
                    .flatMap(postulation -> postulation.getProcess().getCandidatePhases().stream())
                    .map(phase -> new CandidatePhasesResponseDto(
                            phase.getId(),
                            phase.getProcess().getId(),
                            phase.getPhase().getId(),
                            phase.getPhase().getName(),
                            phase.getState().getId(),
                            phase.getState().getName(),
                            phase.getStatus(),
                            phase.getDescription(),
                            phase.getAssignedDate(),
                            phase.getProcess().getPostulation().getVacancyCompany().getId(),
                            phase.getProcess().getPostulation().getVacancyCompany().getRole().getName(),
                            phase.getProcess().getPostulation().getVacancyCompany().getJobProfile().getName()
                    ))
                    .collect(Collectors.toList());

            return new CandidateResponseDto(
                    candidate.getId(),
                    candidate.getName(),
                    candidate.getLastName(),
                    candidate.getCard(),
                    candidate.getBirthdate(),
                    candidate.getRegistrationDate(),
                    candidate.getPhone(),
                    candidate.getCity(),
                    candidate.getEmail(),
                    phases
            );
        }).collect(Collectors.toList());
    }

    public List<CandidateResponseDto> getByNameCandidate(String name){
        List<CandidateEntity> candidateEntities = candidateRepository.findByName(name);

        if(candidateEntities.isEmpty()){
            throw new CandidateNoExistException();
        }

        return candidateEntities.stream()
                .map(mapperCandidateResponse::toDto)
                .collect(Collectors.toList());
    }

    public CandidateResponseDto getByIdCandidate(Long id){
        return candidateRepository.findById(id)
                .map(mapperCandidateResponse::toDto)
                .orElseThrow(CandidateNoExistException::new);
    }

    public List<CandidateResponseDto> getAllCandidate(){
        List<CandidateEntity> candidates = candidateRepository.findAll();

        return candidates.stream()
                .map(candidate -> mapCandidateToResponse(candidate))
                .collect(Collectors.toList());


    }
    private CandidateResponseDto mapCandidateToResponse(CandidateEntity candidate) {
        CandidateResponseDto response = mapperCandidateResponse.toDto(candidate);


        if (candidate.getPostulations() != null) {
            List<CandidatePhasesResponseDto> phases = candidate.getPostulations().stream()
                    .map(PostulationEntity::getProcess)
                    .filter(Objects::nonNull)
                    .flatMap(process -> process.getCandidatePhases().stream()) // Obtiene todas las fases
                    .filter(Objects::nonNull)
                    .map(phasesMappers::toDto)
                    .collect(Collectors.toList());

            response.setPhases(phases);
        }

        return response;
    }

    public CandidateResponseDto saveCandidate(CandidateRequestDto candidateRequestDto) {
        if(candidateRepository.existsByCard(candidateRequestDto.getCard())){
            throw new IdCardAlreadyExistException();
        }

        if(candidateRepository.existsByPhone(candidateRequestDto.getPhone())){
            throw new PhoneAlreadyExistException();
        }

        CandidateEntity candidateEntity = mapperCandidateRequest.toEntity(candidateRequestDto);
        CandidateEntity candidateEntitySave = candidateRepository.save(candidateEntity);

        return mapperCandidateResponse.toDto(candidateEntitySave);
    }

    public Optional<CandidateResponseDto> updateCandidate(Long id, CandidateRequestDto candidateRequestDto) {
        CandidateEntity existingEntity  = candidateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        existingEntity.setName(candidateRequestDto.getName());
        existingEntity.setLastName(candidateRequestDto.getLastName());
        existingEntity.setCard(candidateRequestDto.getCard());
        existingEntity.setBirthdate(candidateRequestDto.getBirthdate());
        existingEntity.setPhone(candidateRequestDto.getPhone());
        existingEntity.setCity(candidateRequestDto.getCity());
        existingEntity.setEmail(candidateRequestDto.getEmail());

        return Optional.of(mapperCandidateResponse.toDto(candidateRepository.save(existingEntity)));
    }

    public void deleteCandidate(Long id){
        CandidateEntity existingCandidate = candidateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        if (postulationRepository.existsByCandidateId(id)) {
            throw new EntityAlreadyHasRelationException();
        }

        candidateRepository.delete(existingCandidate);
    }
    private void validationQuery(String query){
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
    }

    private void validationListPage(Page<CandidateEntity> candidates ){
        if (candidates.isEmpty()) {
            throw new CandidateNoExistException();
        }
    }
    private void validationQueryNumber(String query){
        if (query.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Search query cannot contain numbers");
        }
    }

    private void validationListCandidate(List<CandidateEntity> candidates){
        if (candidates.isEmpty()) {
            throw new CandidateNoExistException();
        }
    }
}
