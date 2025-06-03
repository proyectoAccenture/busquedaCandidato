package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.CandidateRequestDto;
import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.dto.response.CandidateWithPaginationResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.exception.globalmessage.GlobalMessage;
import com.candidateSearch.searching.exception.type.BusinessException;
import com.candidateSearch.searching.mapper.IMapperCandidate;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import com.candidateSearch.searching.service.operationsbusiness.candidate.OperationCandidate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final ICandidateRepository candidateRepository;
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;
    private final IMapperCandidate mapperCandidate;
    private final OperationCandidate operationCandidate;

    public CandidateResponseDto vetaCandidate(String card, String requestedStatus) {
        CandidateEntity candidate = candidateRepository
                .findByCardAndStatusNot(card, Status.INACTIVE)
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        Status currentStatus = candidate.getStatus();
        Status targetStatus;

        Status status;

        try {
            status = Status.valueOf(requestedStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(GlobalMessage.INCORRECT_STATUS);
        }

        if (status == Status.ACTIVE && currentStatus == Status.BLOCKED) {
            targetStatus = Status.ACTIVE;
        } else if (status == Status.BLOCKED && currentStatus == Status.ACTIVE) {
            targetStatus = Status.BLOCKED;
        } else {
            throw new BusinessException(GlobalMessage.INCORRECT_STATUS);
        }

        operationCandidate.changeStatusCascade(
                candidate,
                targetStatus,
                EnumSet.of(Status.ACTIVE, Status.BLOCKED)
        );

        return mapperCandidate.toDto(candidateRepository.save(candidate));
    }


    public List<CandidateResponseDto> getAllCandidateVeto(){
        return candidateRepository.findAll().stream()
                .filter(candidateEntity ->  candidateEntity.getStatus() == Status.BLOCKED)
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

    public CandidateWithPaginationResponseDto getSearchCandidatesFullName(String query, String statusParam) {
        validationQuery(query);
        validationQueryNumber(query);
        Pageable pageable = PageRequest.of(0, 10);

        Status status;

        try {
            status = Status.valueOf(statusParam.toUpperCase());
        } catch (BusinessException e) {
            throw new BusinessException(GlobalMessage.INCORRECT_STATUS);
        }

        String[] keywords = query.toLowerCase().split(" ");

        List<CandidateEntity> candidates;
        if (keywords.length > 1) {
            candidates = candidateRepository.searchByPartialName(keywords[0], keywords[1], pageable);
        } else {
            candidates = candidateRepository.searchByFullName(query, pageable);
        }

        validationListCandidate(candidates);

        List<CandidateResponseDto> candidateDTOs = candidates.stream()
                .filter(candidate -> candidate.getStatus().equals(status))
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
                .orElseThrow(() -> new BusinessException(GlobalMessage.CANDIDATE_DOES_NOT_EXIST));
    }

    public List<CandidateResponseDto> getAllCandidate(){
        return candidateRepository.findAll().stream()
                .filter(candidateEntity ->  candidateEntity.getStatus() == Status.ACTIVE)
                .map(mapperCandidate::toDto)
                .collect(Collectors.toList());
    }

    public CandidateResponseDto saveCandidate(CandidateRequestDto candidateRequestDto) {

        if (candidateRequestDto.getStatus() == Status.INACTIVE ||
                candidateRequestDto.getStatus() == Status.BLOCKED) {
            throw new BusinessException(GlobalMessage.CANNOT_BE_CREATED);
        }

        operationCandidate.validateUniqueFields(candidateRequestDto);

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateRequestDto.getJobProfile())
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_ALREADY_EXISTS));

        OriginEntity originEntity = originRepository.findById(candidateRequestDto.getOrigin())
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        CandidateEntity toSave = operationCandidate.buildCandidate(candidateRequestDto, jobProfileEntity, originEntity);
        CandidateEntity saved = candidateRepository.save(toSave);

        operationCandidate.linkRelations(saved, jobProfileEntity, originEntity);

        return mapperCandidate.toDto(saved);
    }

    @Transactional
    public Optional<CandidateResponseDto> updateCandidate(Long id, CandidateRequestDto candidateDto) {

        CandidateEntity candidateExist  = candidateRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        if (candidateDto.getStatus() == Status.INACTIVE ||
                candidateDto.getStatus() == Status.BLOCKED) {
            throw new BusinessException(GlobalMessage.CANNOT_BE_CREATED);
        }

        if (candidateDto.getStatus() == Status.ACTIVE) {
            operationCandidate.validateUniqueFieldExceptSelf("card", candidateDto.getCard(), id,
                    val -> candidateRepository.findByCardAndStatusNot(val, Status.INACTIVE));

            operationCandidate.validateUniqueFieldExceptSelf("phone", candidateDto.getPhone(), id,
                    val -> candidateRepository.findByPhoneAndStatusNot(val, Status.INACTIVE));

            operationCandidate.validateUniqueFieldExceptSelf("email", candidateDto.getEmail(), id,
                    val -> candidateRepository.findByEmailAndStatusNot(val, Status.INACTIVE));
        }

        if (candidateDto.getStatus() == Status.ACTIVE &&
                (candidateExist.getStatus() == Status.INACTIVE || candidateExist.getStatus() == Status.BLOCKED)) {

            operationCandidate.validateNoOtherActivePostulation(candidateExist.getId());
            operationCandidate.activateCascade(candidateExist.getId());
            candidateExist.setStatus(Status.ACTIVE);
        }

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateDto.getJobProfile())
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        OriginEntity originEntity = originRepository.findById(candidateDto.getOrigin())
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        operationCandidate.updateCandidateField(candidateExist, candidateDto, jobProfileEntity, originEntity);
        CandidateEntity saved = candidateRepository.save(candidateExist);

        operationCandidate.linkRelations(saved, jobProfileEntity, originEntity);

        return Optional.of(mapperCandidate.toDto(saved));
    }

    @Transactional
    public void deleteCandidate(Long id){
        CandidateEntity candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new BusinessException(GlobalMessage.ENTITY_DOES_NOT_EXIST));

        operationCandidate.changeStatusCascade(
                candidate,
                Status.INACTIVE,
                EnumSet.of(Status.ACTIVE)
        );

        candidateRepository.save(candidate);
    }

    private void validationQuery(String query){
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
    }

    private void validationListPage(Page<CandidateEntity> candidates ){
        if (candidates.isEmpty()) {
            throw new BusinessException(GlobalMessage.CANDIDATE_DOES_NOT_EXIST);

        }
    }
    private void validationQueryNumber(String query){
        if (query.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Search query cannot contain numbers");
        }
    }

    private void validationListCandidate(List<CandidateEntity> candidates){
        if (candidates.isEmpty()) {
            throw new BusinessException(GlobalMessage.CANDIDATE_DOES_NOT_EXIST);

        }
    }
}