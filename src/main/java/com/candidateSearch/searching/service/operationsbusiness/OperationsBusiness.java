package com.candidateSearch.searching.service.operationsbusiness;

import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.CandidateStateEntity;
import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.exception.type.CandidateBlockedException;
import com.candidateSearch.searching.exception.type.FieldAlreadyExistException;
import com.candidateSearch.searching.repository.ICandidateStateRepository;
import com.candidateSearch.searching.repository.IPostulationRepository;
import com.candidateSearch.searching.repository.IProcessRepository;
import lombok.RequiredArgsConstructor;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OperationsBusiness {

    private static IPostulationRepository postulationRepository;
    private static  IProcessRepository processRepository;
    private static  ICandidateStateRepository candidateStateRepository;

    public static void validateField(Optional<CandidateEntity> candidateOpt, String fieldName) {
        if (candidateOpt.isPresent()) {
            CandidateEntity existing = candidateOpt.get();
            if (existing.getStatus() == Status.BLOCKED) {
                throw new CandidateBlockedException();
            } else {
                throw new FieldAlreadyExistException(fieldName);
            }
        }
    }

    public static void validateUniqueFieldExceptSelf(String fieldName, String value, Long selfId,
                                                     Function<String, Optional<CandidateEntity>> finder) {
        finder.apply(value).ifPresent(candidate -> {
            if (!candidate.getId().equals(selfId)) {
                if (candidate.getStatus() == Status.BLOCKED) {
                    throw new CandidateBlockedException();
                } else {
                    throw new FieldAlreadyExistException(fieldName);
                }
            }
        });
    }

    private static boolean isInactiveOrBlocked(Status status) {
        return status == Status.INACTIVE || status == Status.BLOCKED;
    }

    public static void activateCascade(Long candidateId) {
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

                    List<ProcessEntity> processes = processRepository.findAllByPostulationId(p.getId());
                    for (ProcessEntity process : processes) {
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
                    }

                } else {
                    if (p.getStatus() != Status.INACTIVE) {
                        p.setStatus(Status.INACTIVE);
                        postulationRepository.save(p);
                    }
                }
            }
        }
    }

    public static void validateNoOtherActivePostulation(Long candidateId) {
        List<PostulationEntity> postulations = postulationRepository.findAllByCandidateId(candidateId);

        boolean hasOtherActive = postulations.stream()
                .anyMatch(p -> p.getStatus() == Status.ACTIVE);

        if (hasOtherActive) {
            throw new FieldAlreadyExistException("Candidate already has a postulation active.");
        }
    }
}
