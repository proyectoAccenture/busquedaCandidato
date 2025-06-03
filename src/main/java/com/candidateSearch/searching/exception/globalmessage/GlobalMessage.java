package com.candidateSearch.searching.exception.globalmessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalMessage {
    ENTITY_ALREADY_EXISTS(GlobalMessage.STATUS_CODE_400, "There is already a entity with that name"),
    ENTITY_DOES_NOT_EXIST(GlobalMessage.STATUS_CODE_400, "There is not entity with that identification"),
    CANDIDATE_DOES_NOT_EXIST(GlobalMessage.STATUS_CODE_400, "The candidate does not exist"),
    CANNOT_BE_CREATED(GlobalMessage.STATUS_CODE_400, "Cannot be create or update, valid the status"),
    CANDIDATE_BLOCKED(GlobalMessage.STATUS_CODE_400, "Candidate is blocked"),
    CANDIDATE_ALREADY_HAVE_POSTULATION(GlobalMessage.STATUS_CODE_400, "Candidate already has a postulation active."),
    INCORRECT_STATUS(GlobalMessage.STATUS_CODE_400, "Invalid status");

    public static final String STATUS_CODE_400 = "400";

    private final String statusCode;
    private final String message;
}