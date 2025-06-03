package com.candidateSearch.searching.service.operationsbusiness.candidate;

import com.candidateSearch.searching.dto.request.CandidateRequestDto;
import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.JobProfileEntity;
import com.candidateSearch.searching.entity.OriginEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.exception.globalmessage.GlobalMessage;
import com.candidateSearch.searching.exception.type.BusinessException;
import com.candidateSearch.searching.exception.type.FieldAlreadyExistException;
import com.candidateSearch.searching.repository.ICandidateRepository;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
import com.candidateSearch.searching.repository.IJobProfileRepository;
import com.candidateSearch.searching.repository.IOriginRepository;
import com.candidateSearch.searching.repository.IPostulationRepository;
import com.candidateSearch.searching.repository.IProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperationCandidate {

    private final IPostulationRepository postulationRepository;
    private final IProcessRepository processRepository;
    private final ICandidateRepository candidateRepository;
    private final ICandidateStateRepository candidateStateRepository;
    private final IJobProfileRepository jobProfileRepository;
    private final IOriginRepository originRepository;

    public void changeStatusCascade(CandidateEntity candidate,
                                           Status targetStatus,
                                           Set<Status> changeOnlyIf) {

        if (changeOnlyIf.contains(candidate.getStatus()) && candidate.getStatus() != targetStatus) {
            candidate.setStatus(targetStatus);
        }

        List<PostulationEntity> posts = postulationRepository.findAllByCandidateId(candidate.getId());
        for (PostulationEntity post : posts) {
            if (changeOnlyIf.contains(post.getStatus()) && post.getStatus() != targetStatus) {
                post.setStatus(targetStatus);
                postulationRepository.save(post);
            }

            processRepository.findByPostulationId(post.getId()).ifPresent(process -> {
                if (changeOnlyIf.contains(process.getStatus()) && process.getStatus() != targetStatus) {
                    process.setStatus(targetStatus);
                    processRepository.save(process);
                }

                List<CandidateStateEntity> states =
                        candidateStateRepository.findByProcessId(process.getId());

                for (CandidateStateEntity st : states) {
                    if (changeOnlyIf.contains(st.getStatusHistory())
                            && st.getStatusHistory() != targetStatus) {
                        st.setStatusHistory(targetStatus);
                        candidateStateRepository.save(st);
                    }
                }
            });
        }
    }

    public void validateField(Optional<CandidateEntity> candidateOpt, String field) {
        if (candidateOpt.isPresent()) {
            CandidateEntity existing = candidateOpt.get();
            if (existing.getStatus() == Status.BLOCKED) {
                throw new BusinessException(GlobalMessage.CANDIDATE_BLOCKED);
            } else {
                throw new FieldAlreadyExistException(field);
            }
        }
    }

    public void validateUniqueFields(CandidateRequestDto dto) {
        validateField(
                candidateRepository.findByCardAndStatusNot(dto.getCard(), Status.INACTIVE),
                "card"
        );

        validateField(
                candidateRepository.findByPhoneAndStatusNot(dto.getPhone(), Status.INACTIVE),
                "phone"
        );

        validateField(
                candidateRepository.findByEmailAndStatusNot(dto.getEmail(), Status.INACTIVE),
                "email"
        );
    }

    public CandidateEntity buildCandidate(CandidateRequestDto dto,
                                                 JobProfileEntity jobProfile,
                                                 OriginEntity origin) {

        CandidateEntity c = new CandidateEntity();
        c.setName(dto.getName());
        c.setLastName(dto.getLastName());
        c.setCard(dto.getCard());
        c.setPhone(dto.getPhone());
        c.setCity(dto.getCity());
        c.setEmail(dto.getEmail());
        c.setBirthdate(dto.getBirthdate());
        c.setSource(dto.getSource());
        c.setSkills(dto.getSkills());
        c.setYearsExperience(dto.getYearsExperience());
        c.setWorkExperience(dto.getWorkExperience());
        c.setSeniority(dto.getSeniority());
        c.setSalaryAspiration(dto.getSalaryAspiration());
        c.setLevel(dto.getLevel());
        c.setDatePresentation(dto.getDatePresentation());
        c.setStatus(dto.getStatus());
        c.setJobProfile(jobProfile);
        c.setOrigin(origin);
        return c;
    }

    public void validateUniqueFieldExceptSelf(String field, String value, Long selfId,
                                                     Function<String, Optional<CandidateEntity>> finder) {
        finder.apply(value).ifPresent(candidate -> {
            if (!candidate.getId().equals(selfId)) {
                if (candidate.getStatus() == Status.BLOCKED) {
                    throw new BusinessException(GlobalMessage.CANDIDATE_BLOCKED);
                } else {
                    throw new FieldAlreadyExistException(field);
                }
            }
        });
    }

    public void validateNoOtherActivePostulation(Long candidateId) {
        List<PostulationEntity> postulations = postulationRepository.findAllByCandidateId(candidateId);

        boolean hasOtherActive = postulations.stream()
                .anyMatch(p -> p.getStatus() == Status.ACTIVE);

        if (hasOtherActive) {
            throw new BusinessException(GlobalMessage.CANDIDATE_ALREADY_HAVE_POSTULATION);
        }
    }

    private boolean isInactiveOrBlocked(Status status) {
        return status == Status.INACTIVE || status == Status.BLOCKED;
    }

    public void activateCascade(Long candidateId) {
        List<PostulationEntity> allPostulation = postulationRepository.findAllByCandidateId(candidateId);

        Map<Long, List<PostulationEntity>> postulationXRole = allPostulation.stream()
                .collect(Collectors.groupingBy(p -> p.getRole().getId()));

        for (Map.Entry<Long, List<PostulationEntity>> entry : postulationXRole.entrySet()) {
            List<PostulationEntity> postulationInARole = entry.getValue();

            PostulationEntity endPostulation = postulationInARole.stream()
                    .max(Comparator.comparing(PostulationEntity::getId))
                    .orElse(null);

            for (PostulationEntity p : postulationInARole) {
                if (p.equals(endPostulation)) {
                    p.setStatus(Status.ACTIVE);
                    postulationRepository.save(p);

                    processRepository.findByPostulationId(p.getId()).ifPresent(process -> {
                        if (isInactiveOrBlocked(process.getStatus())) {
                            process.setStatus(Status.ACTIVE);
                            processRepository.save(process);
                        }

                        List<CandidateStateEntity> states = candidateStateRepository.findByProcessId(process.getId());
                        for (CandidateStateEntity state : states) {
                            if (isInactiveOrBlocked(state.getStatusHistory())) {
                                state.setStatusHistory(Status.ACTIVE);
                                candidateStateRepository.save(state);
                            }
                        }
                    });
                } else {
                    if (p.getStatus() != Status.INACTIVE) {
                        p.setStatus(Status.INACTIVE);
                        postulationRepository.save(p);
                    }
                }
            }
        }
    }

    public static void updateCandidateField(CandidateEntity candidate, CandidateRequestDto dto,
                                           JobProfileEntity jobProfile, OriginEntity origin) {
        candidate.setName(dto.getName());
        candidate.setLastName(dto.getLastName());
        candidate.setCard(dto.getCard());
        candidate.setPhone(dto.getPhone());
        candidate.setCity(dto.getCity());
        candidate.setEmail(dto.getEmail());
        candidate.setBirthdate(dto.getBirthdate());
        candidate.setSource(dto.getSource());
        candidate.setSkills(dto.getSkills());
        candidate.setYearsExperience(dto.getYearsExperience());
        candidate.setWorkExperience(dto.getWorkExperience());
        candidate.setSeniority(dto.getSeniority());
        candidate.setSalaryAspiration(dto.getSalaryAspiration());
        candidate.setLevel(dto.getLevel());
        candidate.setDatePresentation(dto.getDatePresentation());
        candidate.setStatus(Status.ACTIVE);
        candidate.setOrigin(origin);
        candidate.setJobProfile(jobProfile);
    }

    public void linkRelations(CandidateEntity saved,
                                     JobProfileEntity jobProfile,
                                     OriginEntity origin) {

        jobProfile.getCandidates().add(saved);
        jobProfileRepository.save(jobProfile);

        origin.getCandidates().add(saved);
        originRepository.save(origin);
    }
}
