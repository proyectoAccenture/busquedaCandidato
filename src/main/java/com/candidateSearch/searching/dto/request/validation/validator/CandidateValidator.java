package com.candidateSearch.searching.dto.request.validation.validator;

import com.candidateSearch.searching.entity.CandidateEntity;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.exception.type.BadRequestException;
import com.candidateSearch.searching.exception.type.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import java.util.List;

public class CandidateValidator {
    private static final String BLOCK_REASON_REGEX = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ .,]*$";

    private CandidateValidator() {

    }
    public static void validateBlockReason(String blockReason) {
        if (blockReason == null || blockReason.trim().isEmpty()) {
            throw new BadRequestException("Block reason must not be empty when blocking a candidate.");
        }

        if (!blockReason.matches(BLOCK_REASON_REGEX)) {
            throw new BadRequestException("Block reason only allows letters, numbers, spaces, tildes, dots, and commas.");
        }
    }

    public static void validateQueryNotEmpty(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new BadRequestException("Search query cannot be empty");
        }
    }

    public static void validateQueryHasNoNumbers(String query) {
        if (query.matches(".*\\d.*")) {
            throw new BadRequestException("Search query cannot contain numbers");
        }
    }

    public static void validateCandidateListNotEmpty(List<CandidateEntity> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new ResourceNotFoundException("There are no candidates.");
        }
    }

    public static void validateCandidatePageNotEmpty(Page<CandidateEntity> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new ResourceNotFoundException("There are no candidates.");
        }
    }

    public static List<Status> validateStatusesOrDefault(List<Status> statuses) {
        return (statuses == null || statuses.isEmpty())
                ? List.of(Status.ACTIVE, Status.INACTIVE)
                : statuses;
    }

    public static String normalizeQueryNotEmpty(String query) {
        if (query == null || query.isBlank()) {
            return query;
        }
        return query.trim()
                .replaceAll("[áÁ]", "a")
                .replaceAll("[éÉ]", "e")
                .replaceAll("[íÍ]", "i")
                .replaceAll("[óÓ]", "o")
                .replaceAll("[úÚ]", "u");
    }

    public static String validateOrDefaultSortBy(String sortBy) {
        List<String> allowedSortBy = List.of("name","lastName", "datePresentation");
        return allowedSortBy.contains(sortBy) ? sortBy : "name";
    }

    public static Sort.Direction validateOrDefaultDirection(String direction) {
        try {
            return Sort.Direction.fromString(direction.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return Sort.Direction.ASC;
        }
    }
}
