package com.candidateSearch.searching.exception.globalmessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalMessage {
    ENTITY_ALREADY_EXISTS(GlobalMessage.STATUS_CODE_400, "There is already a entity with that name");

    public static final String STATUS_CODE_400 = "400";

    private final String statusCode;
    private final String message;
}