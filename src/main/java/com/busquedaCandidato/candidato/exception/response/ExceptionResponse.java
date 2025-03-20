package com.busquedaCandidato.candidato.exception.response;

public enum ExceptionResponse {
    ENTITY_ALREADY_EXISTS("There is already a entity with that name"),
    PROCESS_NO_EXIST("There is not process with this id"),
    PROCESS_CLOSED("This process is closed"),
    STATE_NO_FOUND("State has not been found"),
    PHASE_NO_FOUND("Phase has not been found"),
    NOT_PHASES_ASSIGNED("There are not phases assigned to this process"),
    ID_CARD_ALREADY_EXISTS("There is already a id card with that number"),
    CANNOT_BE_CREATED_CANDIDATE_PROCESS("the candidate process cannot be create because the post status is false"),
    ENTITY_DOES_NOT_EXIST("There is no entity with that identification"),
    CANDIDATE_DOES_NOT_POSTULATION("The candidate must first have been nominated "),
    PROCESS_ALREADY_EXIST("It already exist a process of the candidate with that postulation"),
    CANDIDATE_DOES_NOT_EXIST("The candidate does not exist"),
    ROLE_ID_DOES_NOT_EXIST("No role id found"),
    PHONE_ALREADY_EXISTS("There is already a candidate with that phone"),
    IT_HAS_RELATION("This entity already has relation with another entity"),
    POSTULATION_IS_OFF("The postulation must be in true"),
    CANNOT_APPLY("You cannot apply, as the application is inactive"),
    IT_ALREADY_EXIST_POSTULATION("You have already submitted an active application"),
    IT_ALREADY_PROCESS_POSTULATION("There is already a process with that id postulation");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}