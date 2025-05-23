package com.candidateSearch.searching.exception.response;

public enum ExceptionResponse {
    ENTITY_ALREADY_EXISTS("There is already a entity with that name"),
    PROCESS_NO_EXIST("There is not process with this id"),
    PROCESS_CLOSED("This process is closed"),
    STATE_NO_FOUND("State has not been found"),
    CANNOT_BE_CREATED("Cannot be create or update, valid the status"),
    ENTITY_DOES_NOT_EXIST("There is not entity with that identification"),
    CANDIDATE_DOES_NOT_POSTULATION("The candidate must first have been nominated "),
    PROCESS_ALREADY_EXIST("It already exist a process of the candidate with that postulation"),
    CANDIDATE_DOES_NOT_EXIST("The candidate does not exist"),
    ROLE_ID_DOES_NOT_EXIST("No role id found"),
    IT_HAS_RELATION("This entity already has relation with another entity"),
    POSTULATION_IS_OFF("The postulation must be in true"),
    CANNOT_APPLY("You cannot apply, as the application is inactive"),
    IT_ALREADY_EXIST_POSTULATION("You have already submitted an active application"),
    IT_ALREADY_PROCESS_POSTULATION("There is already a process with that id postulation"),
    CANDIDATE_BLOCKED("Candidate is blocked"),
    CANNOT_BE_UPDATE("Cannot be update, previous entity is inactive");


    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}