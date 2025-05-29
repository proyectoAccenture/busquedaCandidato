package com.candidateSearch.searching.dto.request.validation.validator;

import com.candidateSearch.searching.entity.utility.Status;

import java.util.List;

public class CandidateStateValidator {
    public CandidateStateValidator() {
    }
    public static List<Status> validateStatusesOrDefault(List<Status> statuses) {
        return (statuses == null || statuses.isEmpty())
                ? List.of(Status.ACTIVE, Status.INACTIVE)
                : statuses;
    }
}
