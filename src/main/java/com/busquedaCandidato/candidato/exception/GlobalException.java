package com.busquedaCandidato.candidato.exception;

import com.busquedaCandidato.candidato.exception.response.ExceptionResponse;
import com.busquedaCandidato.candidato.exception.type.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    private static final String MESSAGE = "Message";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntity.badRequest().body("Invalid date format. Please use 'yyyy-MM-dd'");
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> EntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.ENTITY_ALREADY_EXISTS.getMessage()));
    }

    @ExceptionHandler(ProcessNoExistException.class)
    public ResponseEntity<Map<String, String>> ProcessNoExist(ProcessNoExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PROCESS_NO_EXIST.getMessage()));
    }

    @ExceptionHandler(ProcessClosedException.class)
    public ResponseEntity<Map<String, String>> ProcessClosedException(ProcessClosedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PROCESS_CLOSED.getMessage()));
    }

    @ExceptionHandler(StateNoFoundException.class)
    public ResponseEntity<Map<String, String>> ProcessClosedException(StateNoFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.STATE_NO_FOUND.getMessage()));
    }

    @ExceptionHandler(PhaseNoFoundException.class)
    public ResponseEntity<Map<String, String>> ProcessClosedException(PhaseNoFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PHASE_NO_FOUND.getMessage()));
    }

    @ExceptionHandler(NotPhasesAssignedException.class)
    public ResponseEntity<Map<String, String>> StateAlreadyExistsException(NotPhasesAssignedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NOT_PHASES_ASSIGNED.getMessage()));
    }

    @ExceptionHandler(IdCardAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> IdCardAlreadyExistException(IdCardAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.IDCARD_ALREADY_EXISTS.getMessage()));
    }

    @ExceptionHandler(CannotBeCreateCandidateProcessException.class)
    public ResponseEntity<Map<String, String>> CannotBeCreateCandidateProcessException(CannotBeCreateCandidateProcessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANNOT_BE_CREATED_CANDIDATE_PROCESS.getMessage()));
    }

    @ExceptionHandler(EntityNoExistException.class)
    public ResponseEntity<Map<String, String>> EntityNoExistException(EntityNoExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.ENTITY_DOESNOT_EXIST.getMessage()));
    }

    @ExceptionHandler(CandidateNoPostulationException.class)
    public ResponseEntity<Map<String, String>> CandidateNoPostulationException(CandidateNoPostulationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANDIDATE_DOESNOT_POSTULATION.getMessage()));
    }

    @ExceptionHandler(ProcessAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> ProcessAlreadyExistException(ProcessAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PROCESS_ALREADY_EXIST.getMessage()));
    }

    @ExceptionHandler(CandidateNoExistException.class)
    public ResponseEntity<Map<String, String>> CandidateNoExistException(CandidateNoExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANDIDATE_DOESNOT_EXIST.getMessage()));
    }



}

