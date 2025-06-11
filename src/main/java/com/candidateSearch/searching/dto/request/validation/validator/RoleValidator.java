package com.candidateSearch.searching.dto.request.validation.validator;

import com.candidateSearch.searching.entity.RoleEntity;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.exception.type.BadRequestException;
import com.candidateSearch.searching.exception.type.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import java.util.List;

public class RoleValidator {

    public RoleValidator() {
    }

    public static List<Status> validateStatusesOrDefault(List<Status> statuses) {
        return (statuses == null || statuses.isEmpty())
                ? List.of(Status.ACTIVE)
                : statuses;
    }

    public static void validateQueryNotEmpty(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new BadRequestException("Search query cannot be empty");
        }
    }

    public static void validateCandidatePageNotEmpty(Page<RoleEntity> roleEntities) {
        if (roleEntities== null || roleEntities.isEmpty()) {
            throw new ResourceNotFoundException("There are no Role.");
        }
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
        List<String> allowedSortBy = List.of("nameRole", "assignmentTime");
        return allowedSortBy.contains(sortBy) ? sortBy : "nameRole";
    }

    public static Sort.Direction validateOrDefaultDirection(String direction) {
        try {
            return Sort.Direction.fromString(direction.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return Sort.Direction.ASC;
        }
    }
}
