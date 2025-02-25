package com.busquedaCandidato.candidato.exception;

import com.busquedaCandidato.candidato.exception.response.ExceptionResponse;
import com.busquedaCandidato.candidato.exception.type.StateAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    private static final String MESSAGE = "Message";

    @ExceptionHandler(StateAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> StateAlreadyExistsException(StateAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.STATE_ALREADY_EXISTS.getMessage()));
    }
}

