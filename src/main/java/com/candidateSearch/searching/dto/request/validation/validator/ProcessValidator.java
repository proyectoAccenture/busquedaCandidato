package com.candidateSearch.searching.dto.request.validation.validator;

import com.candidateSearch.searching.entity.ProcessEntity;
import com.candidateSearch.searching.entity.utility.Status;
import com.candidateSearch.searching.exception.type.CustomBadRequestException;
import com.candidateSearch.searching.exception.type.CustomNotFoundException;
import com.candidateSearch.searching.repository.IPostulationRepository;

import java.util.List;
import java.util.Optional;

public class ProcessValidator {
    public ProcessValidator() {
    }
    public static void validateLongId(Long id, IPostulationRepository repository) {
        if (id == null || id <= 0) {
            throw new CustomBadRequestException("The application ID must be a positive number.");
        }
        if (!repository.existsById(id)) {
            throw new CustomNotFoundException("No postulation found with ID: " + id);
        }
    }

    public static void validateProcessListNotEmpty(List<ProcessEntity> processes) {
        if (processes == null || processes.isEmpty()) {
            throw new CustomNotFoundException("No processes found for the given search criteria.");
        }
    }

    public static void validateProcessOptionalNotEmpty(Optional<ProcessEntity> optionalProcess) {
        if (optionalProcess.isEmpty()) {
            throw new CustomNotFoundException("No processes found for the given search criteria.");
        }
    }

    public static void validateStringQueryNotEmpty(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new CustomBadRequestException("The search query cannot be empty.");
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
                ? List.of(Status.ACTIVE, Status.INACTIVE)
                : statuses;
    }
}
