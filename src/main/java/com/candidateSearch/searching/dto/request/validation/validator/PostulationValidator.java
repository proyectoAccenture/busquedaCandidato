package com.candidateSearch.searching.dto.request.validation.validator;

import com.candidateSearch.searching.entity.PostulationEntity;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.exception.type.BadRequestException;
import com.candidateSearch.searching.exception.type.ResourceNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public class PostulationValidator {
    public PostulationValidator() {
    }
    public static void validatePostulationListNotEmpty(List<PostulationEntity> postulations) {
        if (postulations == null || postulations.isEmpty()) {
            throw new ResourceNotFoundException("No postulations found for the given search criteria.");
        }
    }

    public static void validatePostulationPageNotEmpty(Page<PostulationEntity> postulations) {
        if (postulations == null || postulations.isEmpty()) {
            throw new ResourceNotFoundException("No postulations found for the given search criteria.");
        }
    }

    public static void validateQueryNotEmpty(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new BadRequestException("The search query cannot be empty.");
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
    public static List<Status> validateStatusesOrDefault(List<Status> statuses) {
        return (statuses == null || statuses.isEmpty())
                ? List.of(Status.ACTIVE)
                : statuses;
    }
}
