package com.busquedaCandidato.candidato.exception;

import com.busquedaCandidato.candidato.exception.response.ExceptionResponse;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    private static final String MESSAGE = "Message";

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> EntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.ENTITY_ALREADY_EXISTS.getMessage()));
    }
}

