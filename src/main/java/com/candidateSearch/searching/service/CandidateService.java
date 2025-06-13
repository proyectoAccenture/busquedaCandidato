package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.CandidateRequestDto;
import com.candidateSearch.searching.dto.request.validation.validator.CandidateValidator;
import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.dto.response.PaginationResponseDto;
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
import org.springframework.data.domain.Sort;
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

    public CandidateResponseDto vetaCandidate(String card, String requestedStatus,String blockReason) {
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
            candidate.setBlockReason(null);
        } else if (status == Status.BLOCKED && currentStatus == Status.ACTIVE) {
            CandidateValidator.validateBlockReason(blockReason);
            targetStatus = Status.BLOCKED;
            candidate.setBlockReason(blockReason);
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

    public PaginationResponseDto<CandidateResponseDto> getSearchCandidates(String query, List<Status> statuses, int page, int size, String sortBy, String direction) {
        CandidateValidator.validateQueryNotEmpty(query);
        query = CandidateValidator.normalizeQueryNotEmpty(query);

        String validSortBy = CandidateValidator.validateOrDefaultSortBy(sortBy);
        Sort.Direction sortDirection = CandidateValidator.validateOrDefaultDirection(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, validSortBy));

        String[] words = query.split(" ");
        String word1 = words.length > 0 ? words[0] : null;
        String word2 = words.length > 1 ? words[1] : null;
        String word3 = words.length > 2 ? words[2] : null;
        String word4 = words.length > 3 ? words[3] : null;

        Page<CandidateEntity> candidates = candidateRepository.searchCandidates(
                word1,
                word2,
                word3,
                word4,
                query,
                CandidateValidator.validateStatusesOrDefault(statuses),
                pageable);

        CandidateValidator.validateCandidatePageNotEmpty(candidates);
        List<CandidateResponseDto> candidateDTOs = candidates.getContent().stream()
                .map(mapperCandidate::toDto)
                .toList();

        return new PaginationResponseDto<>(
                candidateDTOs,
                candidates.getNumber(),
                candidates.getSize(),
                candidates.getTotalPages(),
                candidates.getTotalElements()
        );
    }

    public PaginationResponseDto<CandidateResponseDto> getSearchCandidatesFullName(String query, String statusParam) {
        CandidateValidator.validateQueryNotEmpty(query);
        CandidateValidator.validateQueryHasNoNumbers(query);
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

        CandidateValidator.validateCandidateListNotEmpty(candidates);
        List<CandidateResponseDto> candidateDTOs = candidates.stream()
                .filter(candidate -> candidate.getStatus().equals(status))
                .map(mapperCandidate::toDto)
                .toList();

        return new PaginationResponseDto<>(
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

    public PaginationResponseDto<CandidateResponseDto> getAllCandidate(List<Status> statuses, int page, int size, String sortBy, String direction){
        String validSortBy = CandidateValidator.validateOrDefaultSortBy(sortBy);
        Sort.Direction sortDirection = CandidateValidator.validateOrDefaultDirection(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, validSortBy));

        Page<CandidateEntity> candidatePage = candidateRepository.findByStatusIn(
                CandidateValidator.validateStatusesOrDefault(statuses),
                pageable);

        List<CandidateResponseDto> candidates = candidatePage.getContent()
                .stream()
                .map(mapperCandidate::toDto)
                .toList();

        return new PaginationResponseDto<>(
                candidates,
                candidatePage.getNumber(),
                candidatePage.getSize(),
                candidatePage.getTotalPages(),
                candidatePage.getTotalElements()
        );
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
}