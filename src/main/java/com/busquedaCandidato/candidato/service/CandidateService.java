package com.busquedaCandidato.candidato.service;

import com.busquedaCandidato.candidato.dto.request.CandidateRequestDto;
import com.busquedaCandidato.candidato.dto.response.CandidateResponse;
import com.busquedaCandidato.candidato.dto.response.CandidateResponseDto;
import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.JobProfileEntity;
import com.busquedaCandidato.candidato.entity.OriginEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import com.busquedaCandidato.candidato.exception.type.CandidateNoExistException;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyHasRelationException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.exception.type.IdCardAlreadyExistException;
import com.busquedaCandidato.candidato.exception.type.PhoneAlreadyExistException;
import com.busquedaCandidato.candidato.exception.type.RoleIdNoExistException;
import com.busquedaCandidato.candidato.mapper.IMapperCandidateResponse;
import com.busquedaCandidato.candidato.repository.ICandidateRepository;
import com.busquedaCandidato.candidato.repository.IJobProfileRepository;
import com.busquedaCandidato.candidato.repository.IOriginRepository;
import com.busquedaCandidato.candidato.repository.IPostulationRepository;
import com.busquedaCandidato.candidato.repository.IRoleIDRepository;
import com.busquedaCandidato.candidato.repository.IVacancyCompanyRepository;
import lombok.RequiredArgsConstructor;
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
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;
    private final IMapperCandidateResponse mapperCandidateResponse;

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

        String[] keywords = query.toLowerCase().split(" ");

        List<CandidateEntity> candidates;
        if (keywords.length > 1) {
            candidates = candidateRepository.searchByPartialName(keywords[0], keywords[1], pageable);
        } else {
            candidates = candidateRepository.searchByFullName(query, pageable);
        }

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

    public CandidateResponseDto getByIdCandidate(Long id){
        return candidateRepository.findById(id)
                .map(mapperCandidateResponse::toDto)
                .orElseThrow(CandidateNoExistException::new);
    }

    public List<CandidateResponseDto> getAllCandidate(){
        List<CandidateEntity> candidates = candidateRepository.findAll();

        return candidates.stream()
                .map(mapperCandidateResponse::toDto)
                .collect(Collectors.toList());
    }

    public CandidateResponseDto saveCandidate(CandidateRequestDto candidateRequestDto) {
        if(candidateRepository.existsByCard(candidateRequestDto.getCard())){
            throw new IdCardAlreadyExistException();
        }

        if(candidateRepository.existsByPhone(candidateRequestDto.getPhone())){
            throw new PhoneAlreadyExistException();
        }

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateRequestDto.getJobProfile())
                .orElseThrow(EntityNoExistException::new);

        OriginEntity originEntity = originRepository.findById(candidateRequestDto.getOrigin())
                .orElseThrow(EntityNoExistException::new);

        CandidateEntity candidateEntityNew = new CandidateEntity();
        candidateEntityNew.setName(candidateRequestDto.getName());
        candidateEntityNew.setLastName(candidateRequestDto.getLastName());
        candidateEntityNew.setCard(candidateRequestDto.getCard());
        candidateEntityNew.setPhone(candidateRequestDto.getPhone());
        candidateEntityNew.setCity(candidateRequestDto.getCity());
        candidateEntityNew.setEmail(candidateRequestDto.getEmail());
        candidateEntityNew.setBirthdate(candidateRequestDto.getBirthdate());
        candidateEntityNew.setSource(candidateRequestDto.getSource());
        candidateEntityNew.setSkills(candidateRequestDto.getSkills());
        candidateEntityNew.setYearsExperience(candidateRequestDto.getYearsExperience());
        candidateEntityNew.setWorkExperience(candidateRequestDto.getWorkExperience());
        candidateEntityNew.setSeniority(candidateRequestDto.getSeniority());
        candidateEntityNew.setSalaryAspiration(candidateRequestDto.getSalaryAspiration());
        candidateEntityNew.setLevel(candidateRequestDto.getLevel());
        candidateEntityNew.setDatePresentation(candidateRequestDto.getDatePresentation());
        candidateEntityNew.setOrigin(originEntity);
        candidateEntityNew.setJobProfile(jobProfileEntity);

        CandidateEntity candidateEntitySave = candidateRepository.save(candidateEntityNew);
        return mapperCandidateResponse.toDto(candidateEntitySave);
    }

    public Optional<CandidateResponseDto> updateCandidate(Long id, CandidateRequestDto candidateRequestDto) {
        CandidateEntity existingEntity  = candidateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateRequestDto.getJobProfile())
                .orElseThrow(EntityNoExistException::new);

        OriginEntity originEntity = originRepository.findById(candidateRequestDto.getOrigin())
                .orElseThrow(EntityNoExistException::new);

        existingEntity.setName(candidateRequestDto.getName());
        existingEntity.setLastName(candidateRequestDto.getLastName());
        existingEntity.setCard(candidateRequestDto.getCard());
        existingEntity.setPhone(candidateRequestDto.getPhone());
        existingEntity.setCity(candidateRequestDto.getCity());
        existingEntity.setEmail(candidateRequestDto.getEmail());
        existingEntity.setBirthdate(candidateRequestDto.getBirthdate());
        existingEntity.setSource(candidateRequestDto.getSource());
        existingEntity.setSkills(candidateRequestDto.getSkills());
        existingEntity.setYearsExperience(candidateRequestDto.getYearsExperience());
        existingEntity.setWorkExperience(candidateRequestDto.getWorkExperience());
        existingEntity.setSeniority(candidateRequestDto.getSeniority());
        existingEntity.setSalaryAspiration(candidateRequestDto.getSalaryAspiration());
        existingEntity.setLevel(candidateRequestDto.getLevel());
        existingEntity.setDatePresentation(candidateRequestDto.getDatePresentation());
        existingEntity.setOrigin(originEntity);
        existingEntity.setJobProfile(jobProfileEntity);

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
