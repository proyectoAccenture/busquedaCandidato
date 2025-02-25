package com.busquedaCandidato.candidato.exception.response;

public enum ExceptionResponse {
    STATE_ALREADY_EXISTS("There is already a status with that name");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}