package com.candidateSearch.searching.exception.response;

import lombok.Getter;

@Getter
public enum ErrorCatalog {
    NOT_FOUND("ERR_001", "Not found"),
    INVALID_PARAMETERS("ERR_002", "Invalid parameters"),
    READY_EXIST("ERR_003", "Resource already exists"),
    INFO_BAD_REQUEST("ERR_004", "Bad request"),
    DATABASE_ERROR("ERR_005", "Error accessing the database"),
    GENERIC_ERROR("ERR_999", "An unexpected error occurred");

    private final String code;
    private final String message;

    ErrorCatalog(String code, String message) {
        this.code = code;
        this.message = message;

    }
}
