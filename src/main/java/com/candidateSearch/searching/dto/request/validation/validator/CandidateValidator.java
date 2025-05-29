package com.candidateSearch.searching.dto.request.validation.validator;

import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.exception.type.CustomBadRequestException;
import com.candidateSearch.searching.exception.type.CustomNotFoundException;
import org.springframework.data.domain.Page;
import java.util.List;

public class CandidateValidator {
    private CandidateValidator() {
        // Previene la instanciación
    }
    public static void validateQueryNotEmpty(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new CustomBadRequestException("Search query cannot be empty");
        }
    }

    public static void validateQueryHasNoNumbers(String query) {
        if (query.matches(".*\\d.*")) {
            throw new CustomBadRequestException("Search query cannot contain numbers");
        }
    }

    public static void validateCandidateListNotEmpty(List<CandidateEntity> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new CustomNotFoundException("There are no candidates.");
        }
    }

    public static void validateCandidatePageNotEmpty(Page<CandidateEntity> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new CustomNotFoundException("There are no candidates.");
        }
    }

    public static List<Status> validateStatusesOrDefault(List<Status> statuses) {
        return (statuses == null || statuses.isEmpty())
                ? List.of(Status.ACTIVE, Status.INACTIVE)
                : statuses;
    }


}
