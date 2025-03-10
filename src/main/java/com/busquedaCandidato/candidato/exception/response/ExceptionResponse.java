package com.busquedaCandidato.candidato.exception.response;

public enum ExceptionResponse {
    ENTITY_ALREADY_EXISTS("There is already a entity with that name"),
    PROCESS_NO_EXIST("There is not process with this id"),
    PROCESS_CLOSED("This process is closed"),
    STATE_NO_FOUND("State has not been found"),
    PHASE_NO_FOUND("Phase has not been found"),
    NOT_PHASES_ASSIGNED("There are not phases assigned to this process"),
    IDCARD_ALREADY_EXISTS("There is already a id card with that number"),
    CANNOT_BE_CREATED_CANDIDATE_PROCESS("the candidate process cannot be create because the post status is false"),
    ENTITY_DOESNOT_EXIST("There is anything entity with that id "),
    CANDIDATE_DOESNOT_POSTULATION("The candidate must first have been nominated "),
    PROCESS_ALREADY_EXIST("It already exist a process of the candidate with that postulation"),
    CANDIDATE_DOESNOT_EXIST("The candidate does not exist");


    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}