package com.candidateSearch.searching.service;

import com.candidateSearch.searching.dto.request.CandidateRequestDto;
import com.candidateSearch.searching.dto.request.validation.validator.CandidateValidator;
import com.candidateSearch.searching.dto.response.CandidateResponseDto;
import com.candidateSearch.searching.dto.response.PaginationResponseDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.exception.type.*;
import com.candidateSearch.searching.mapper.IMapperCandidate;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import com.candidateSearch.searching.repository.IPostulationRepository;
import com.candidateSearch.searching.repository.IProcessRepository;
import com.candidateSearch.searching.repository.IRoleRepository;
import com.candidateSearch.searching.entity.utility.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final ICandidateRepository candidateRepository;
    private final IPostulationRepository postulationRepository;
    private final IRoleRepository roleRepository;
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;
    private final IProcessRepository processRepository;
    private final ICandidateStateRepository candidateStateRepository;
    private final IMapperCandidate mapperCandidate;

    public CandidateResponseDto vetaCandidate(String card){
        CandidateEntity candidate = candidateRepository
                .findByCardAndStatusNot(card, Status.INACTIVE)
                .orElseThrow(()-> new CustomNotFoundException(
                        "The candidate with that card does not exist."));

        candidate.setStatus(Status.BLOCKED);

        List<PostulationEntity> postulationFind = postulationRepository.findAllByCandidateId(candidate.getId());
        if (postulationFind != null) {
            for (PostulationEntity postulation : postulationFind) {
                if (postulation.getStatus().equals(Status.ACTIVE)) {
                    postulation.setStatus(Status.INACTIVE);
                    postulationRepository.save(postulation);
                }

                Optional<ProcessEntity> processFind = processRepository.findByPostulationId(postulation.getId());
                if (processFind.isPresent()) {
                    ProcessEntity process = processFind.get();
                    if (process.getStatus().equals(Status.ACTIVE)) {
                        process.setStatus(Status.INACTIVE);
                        processRepository.save(process);
                    }

                    List<CandidateStateEntity> candidateStateFind = candidateStateRepository.findByProcessId(process.getId());
                    if (candidateStateFind != null) {
                        for (CandidateStateEntity candidateState : candidateStateFind) {
                            if (candidateState.getStatusHistory().equals(Status.ACTIVE)) {
                                candidateState.setStatusHistory(Status.INACTIVE);
                                candidateStateRepository.save(candidateState);
                            }
                        }
                    }
                }
            }
        }

        return mapperCandidate.toDto(candidateRepository.save(candidate));
    }

    public List<CandidateResponseDto> getAllCandidateVeto(){
        return candidateRepository.findAll().stream()
                .filter(candidateEntity ->  candidateEntity.getStatus() == Status.BLOCKED)
                .map(mapperCandidate::toDto)
                .collect(Collectors.toList());
    }

    public List<CandidateResponseDto> getCandidateByRole(String roleName) {

        RoleEntity role = roleRepository.findByNameRole(roleName)
                .orElseThrow(()-> new CustomNotFoundException(
                        "The role does not exist."));

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

    public PaginationResponseDto<CandidateResponseDto> getSearchCandidates(String query, int page, int size) {
        CandidateValidator.validateQueryNotEmpty(query);
        Pageable pageable = PageRequest.of(page, size);

        Page<CandidateEntity> candidates = candidateRepository.searchCandidates(query, pageable);
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
    public PaginationResponseDto<CandidateResponseDto> getSearchCandidatesV2(String query, int page, int size, List<Status> statuses) {
        CandidateValidator.validateQueryNotEmpty(query);
        query = CandidateValidator.normalizeQueryNotEmpty(query);

        Pageable pageable = PageRequest.of(page, size);
        String[] words = query.split(" ");
        String word1 = words.length > 0 ? words[0] : null;
        String word2 = words.length > 1 ? words[1] : null;
        String word3 = words.length > 2 ? words[2] : null;
        String word4 = words.length > 3 ? words[3] : null;

        Page<CandidateEntity> candidates = candidateRepository.searchCandidatesV2(
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


    public PaginationResponseDto<CandidateResponseDto> getSearchCandidatesFullName(String query) {
        CandidateValidator.validateQueryNotEmpty(query);
        CandidateValidator.validateQueryHasNoNumbers(query);
        Pageable pageable = PageRequest.of(0, 10);

        String[] keywords = query.toLowerCase().split(" ");

        List<CandidateEntity> candidates;
        if (keywords.length > 1) {
            candidates = candidateRepository.searchByPartialName(keywords[0], keywords[1], pageable);
        } else {
            candidates = candidateRepository.searchByFullName(query, pageable);
        }

        CandidateValidator.validateCandidateListNotEmpty(candidates);

        List<CandidateResponseDto> candidateDTOs = candidates.stream()
                .map(mapperCandidate::toDto)
                .toList();

        return new PaginationResponseDto<>(candidateDTOs, 0, candidateDTOs.size(), 1, candidateDTOs.size());
    }

    public PaginationResponseDto<CandidateResponseDto> getSearchCandidatesFullNameV2(String query, int page, int size, List<Status> statuses) {
        CandidateValidator.validateQueryNotEmpty(query);
        CandidateValidator.validateQueryHasNoNumbers(query);

        Pageable pageable = PageRequest.of(page, size);
        String[] keywords = query.toLowerCase().split(" ");

        Page<CandidateEntity> candidatePage;
        if (keywords.length > 1) {
            candidatePage = candidateRepository.searchByPartialNameV2(
                    keywords[0],
                    keywords[1],
                    CandidateValidator.validateStatusesOrDefault(statuses),
                    pageable
            );
        } else {
            candidatePage = candidateRepository.searchByFullNameV2(
                    query,
                    CandidateValidator.validateStatusesOrDefault(statuses),
                    pageable
            );
        }

        CandidateValidator.validateCandidateListNotEmpty(candidatePage.getContent());

        List<CandidateResponseDto> candidateDTOs = candidatePage.getContent()
                .stream()
                .map(mapperCandidate::toDto)
                .toList();

        return new PaginationResponseDto<>(
                candidateDTOs,
                candidatePage.getNumber(),
                candidatePage.getSize(),
                candidatePage.getTotalPages(),
                candidatePage.getTotalElements()
        );
    }



    public CandidateResponseDto getByIdCandidate(Long id){
        return candidateRepository.findById(id)
                .map(mapperCandidate::toDto)
                .orElseThrow(()-> new CustomNotFoundException("There is no candidate with that ID."));
    }

    public List<CandidateResponseDto> getAllCandidate(){
        return candidateRepository.findAll().stream()
                .filter(candidateEntity ->  candidateEntity.getStatus() != Status.BLOCKED)
                .map(mapperCandidate::toDto)
                .collect(Collectors.toList());
    }

    public PaginationResponseDto<CandidateResponseDto> getAllCandidates(List<Status> statuses, int page, int size) {
           Pageable pageable = PageRequest.of(page, size);

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
            throw new CustomBadRequestException("Cannot be create or update, valid the status.");
        }

        Optional<CandidateEntity> existingOptCard = candidateRepository.findByCardAndStatusNot(candidateRequestDto.getCard(), Status.INACTIVE);

        if(existingOptCard.isPresent()){
            CandidateEntity existing = existingOptCard.get();

            if (existing.getStatus() == Status.BLOCKED) {
                throw new CustomConflictException("Candidate is blocked.");
            } else {
                throw new CustomConflictException("There is already a card with that parameter.");
            }
        }

        Optional<CandidateEntity> existingOptPhone = candidateRepository.findByPhoneAndStatusNot(candidateRequestDto.getPhone(), Status.INACTIVE);

        if(existingOptPhone.isPresent()){
            CandidateEntity existing = existingOptPhone.get();

            if (existing.getStatus() == Status.BLOCKED) {
                throw new CustomConflictException("Candidate is blocked.");
            } else {
                throw new CustomConflictException("There is already a phone with that parameter.");
            }
        }

        Optional<CandidateEntity> existingOptEmail = candidateRepository.findByEmailAndStatusNot(candidateRequestDto.getEmail(), Status.INACTIVE);

        if(existingOptEmail.isPresent()){
            CandidateEntity existing = existingOptEmail.get();

            if (existing.getStatus() == Status.BLOCKED) {
                throw new CustomConflictException("Candidate is blocked.");
            } else {
                throw new CustomConflictException("There is already a email with that parameter.");
            }
        }

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateRequestDto.getJobProfile())
                .orElseThrow(()-> new CustomNotFoundException("That job profile does not exist."));

        OriginEntity originEntity = originRepository.findById(candidateRequestDto.getOrigin())
                .orElseThrow(()-> new CustomNotFoundException("There is no origin with that ID."));

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
        candidateEntityNew.setStatus(candidateRequestDto.getStatus());
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
                .orElseThrow(()-> new CustomNotFoundException("There is no candidate with that ID."));

        if(candidateRequestDto.getStatus() == Status.ACTIVE) {
            List<PostulationEntity> postulationFind = postulationRepository.findAllByCandidateId(id);
            if (postulationFind != null) {
                for (PostulationEntity postulation : postulationFind) {
                    if (postulation.getStatus().equals(Status.INACTIVE) || postulation.getStatus().equals(Status.BLOCKED)) {
                        postulation.setStatus(Status.ACTIVE);
                        postulationRepository.save(postulation);
                    }

                    List<ProcessEntity> processAllFind = processRepository.findAllByPostulationId(postulation.getId());
                    if (processAllFind != null) {
                        for (ProcessEntity process : processAllFind){
                            if (process.getStatus().equals(Status.INACTIVE) || process.getStatus().equals(Status.BLOCKED)) {
                                process.setStatus(Status.ACTIVE);
                                processRepository.save(process);
                            }

                            List<CandidateStateEntity> candidateStateFind = candidateStateRepository.findByProcessId(process.getId());
                            if (candidateStateFind != null) {
                                for (CandidateStateEntity candidateState : candidateStateFind) {
                                    if (candidateState.getStatusHistory().equals(Status.INACTIVE) || candidateState.getStatusHistory().equals(Status.BLOCKED)) {
                                        candidateState.setStatusHistory(Status.ACTIVE);
                                        candidateStateRepository.save(candidateState);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (candidateRequestDto.getStatus() == Status.INACTIVE ||
                candidateRequestDto.getStatus() == Status.BLOCKED) {
            throw new CustomBadRequestException("Cannot be create or update, valid the status.");
        }

        JobProfileEntity jobProfileEntity = jobProfileRepository.findById(candidateRequestDto.getJobProfile())
                .orElseThrow(()-> new CustomNotFoundException("There is no job profile with that ID."));

        OriginEntity originEntity = originRepository.findById(candidateRequestDto.getOrigin())
                .orElseThrow(()-> new CustomNotFoundException("There is no origin with that ID."));


        if (candidateRequestDto.getStatus() == Status.ACTIVE) {
            validateUniqueFieldExceptSelf("card", candidateRequestDto.getCard(), id,
                    val -> candidateRepository.findByCardAndStatusNot(val, Status.INACTIVE));

            validateUniqueFieldExceptSelf("phone", candidateRequestDto.getPhone(), id,
                    val -> candidateRepository.findByPhoneAndStatusNot(val, Status.INACTIVE));

            validateUniqueFieldExceptSelf("email", candidateRequestDto.getEmail(), id,
                    val -> candidateRepository.findByEmailAndStatusNot(val, Status.INACTIVE));
        }

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
        existingEntity.setStatus(Status.ACTIVE);
        existingEntity.setOrigin(originEntity);
        existingEntity.setJobProfile(jobProfileEntity);

        CandidateEntity candidateSaved = candidateRepository.save(existingEntity);

        jobProfileEntity.getCandidates().add(candidateSaved);
        jobProfileRepository.save(jobProfileEntity);

        assert originEntity != null;
        originEntity.getCandidates().add(candidateSaved);
        originRepository.save(originEntity);

        return Optional.of(mapperCandidate.toDto(candidateSaved));
    }

    @Transactional
    public void deleteCandidate(Long id){
        CandidateEntity existingCandidate = candidateRepository.findById(id)
                .orElseThrow(()-> new CustomNotFoundException("There is no candidate with that ID."));

        existingCandidate.setStatus(Status.INACTIVE);

        List<PostulationEntity> postulationFind = postulationRepository.findAllByCandidateId(id);
        if (postulationFind != null) {
            for (PostulationEntity postulation : postulationFind) {
                if (postulation.getStatus().equals(Status.ACTIVE)) {
                    postulation.setStatus(Status.INACTIVE);
                    postulationRepository.save(postulation);
                }

                Optional<ProcessEntity> processFind = processRepository.findByPostulationId(postulation.getId());
                if (processFind.isPresent()) {
                    ProcessEntity process = processFind.get();
                    if (process.getStatus().equals(Status.ACTIVE)) {
                        process.setStatus(Status.INACTIVE);
                        processRepository.save(process);
                    }

                    List<CandidateStateEntity> candidateStateFind = candidateStateRepository.findByProcessId(process.getId());
                    if (candidateStateFind != null) {
                        for (CandidateStateEntity candidateState : candidateStateFind) {
                            if (candidateState.getStatusHistory().equals(Status.ACTIVE)) {
                                candidateState.setStatusHistory(Status.INACTIVE);
                                candidateStateRepository.save(candidateState);
                            }
                        }
                    }
                }
            }
        }
        candidateRepository.save(existingCandidate);
    }

    private void validateUniqueFieldExceptSelf(String fieldName, String value, Long selfId,
                                               Function<String, Optional<CandidateEntity>> finder) {
        finder.apply(value).ifPresent(candidate -> {
            if (!candidate.getId().equals(selfId)) {
                if (candidate.getStatus() == Status.BLOCKED) {
                    throw new CustomConflictException("Candidate is blocked");
                } else {
                    throw new CustomConflictException("There is already a "+fieldName+" with that parameter.");
                }
            }
        });
    }
}