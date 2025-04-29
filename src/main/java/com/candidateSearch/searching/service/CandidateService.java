package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.CandidateRequestDto;
import com.candidateSearch.searching.dto.response.CandidateWithPaginationResponseDto;
import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.exception.type.CandidateNoExistException;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.exception.type.FieldAlreadyExistException;
import com.candidateSearch.searching.exception.type.RoleIdNoExistException;
import com.candidateSearch.searching.mapper.IMapperCandidate;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import com.candidateSearch.searching.repository.IPostulationRepository;
import com.candidateSearch.searching.repository.IRoleRepository;
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
    private final IPostulationRepository postulationRepository;
    private final IRoleRepository roleRepository;
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;
    private final IMapperCandidate mapperCandidate;

    public List<CandidateResponseDto> getCandidateByRole(String roleName) {

        RoleEntity role = roleRepository.findByNameRole(roleName)
                .orElseThrow(RoleIdNoExistException::new);

        List<PostulationEntity> postulations = postulationRepository.findByRole(role);

        List<Long> candidateIds = postulations.stream()
                .map(PostulationEntity::getCandidate)
                .filter(Objects::nonNull)
                .map(CandidateEntity::getId)
                .distinct()
                .toList();

        List<CandidateEntity> candidates = candidateRepository.findByIdIn(candidateIds);

        return candidates.stream()
                .map(mapperCandidate::toDto)
                .collect(Collectors.toList());

    }

    public CandidateWithPaginationResponseDto getSearchCandidates(String query, int page, int size) {
        validationQuery(query);
        Pageable pageable = PageRequest.of(page, size);

        Page<CandidateEntity> candidates = candidateRepository.searchCandidates(query, pageable);
        validationListPage(candidates);

        List<CandidateResponseDto> candidateDTOs = candidates.getContent().stream()
                .map(mapperCandidate::toDto)
                .toList();

        return new CandidateWithPaginationResponseDto(
                candidateDTOs,
                candidates.getNumber(),
                candidates.getSize(),
                candidates.getTotalPages(),
                candidates.getTotalElements()
        );
    }

    public CandidateWithPaginationResponseDto getSearchCandidatesFullName(String query) {
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
                .map(mapperCandidate::toDto)
                .toList();

        return new CandidateWithPaginationResponseDto(
                candidateDTOs,
                0,
                candidateDTOs.size(),
                1,
                candidateDTOs.size()
        );
    }

    public CandidateResponseDto getByIdCandidate(Long id){
        return candidateRepository.findById(id)
                .map(mapperCandidate::toDto)
                .orElseThrow(CandidateNoExistException::new);
    }

    public List<CandidateResponseDto> getAllCandidate(){
        List<CandidateEntity> candidates = candidateRepository.findAll();

        return candidates.stream()
                .map(mapperCandidate::toDto)
                .collect(Collectors.toList());
    }

    public CandidateResponseDto saveCandidate(CandidateRequestDto candidateRequestDto) {
        if(candidateRepository.existsByCard(candidateRequestDto.getCard())){
            throw new FieldAlreadyExistException("card");
        }

        if(candidateRepository.existsByPhone(candidateRequestDto.getPhone())){
            throw new FieldAlreadyExistException("phone");
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

        jobProfileEntity.getCandidates().add(candidateEntitySave);
        jobProfileRepository.save(jobProfileEntity);

        originEntity.getCandidates().add(candidateEntitySave);
        originRepository.save(originEntity);

        return mapperCandidate.toDto(candidateEntitySave);
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

        CandidateEntity candidateSaved = candidateRepository.save(existingEntity);

        jobProfileEntity.getCandidates().add(candidateSaved);
        jobProfileRepository.save(jobProfileEntity);

        originEntity.getCandidates().add(candidateSaved);
        originRepository.save(originEntity);

        return Optional.of(mapperCandidate.toDto(candidateSaved));
    }

    public void deleteCandidate(Long id){
        CandidateEntity existingCandidate = candidateRepository.findById(id)
                .orElseThrow(EntityNoExistException::new);

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
