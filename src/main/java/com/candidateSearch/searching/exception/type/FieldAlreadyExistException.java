package com.candidateSearch.searching.exception.type;

public class FieldAlreadyExistException extends RuntimeException {
    public FieldAlreadyExistException(String field) {
        super("There is already a " + field + " with that parameter");
    }
}
