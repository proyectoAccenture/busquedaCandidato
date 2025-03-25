package com.busquedaCandidato.candidato.exception;

import com.busquedaCandidato.candidato.exception.response.ExceptionResponse;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyExistsException;
import com.busquedaCandidato.candidato.exception.type.ProcessClosedException;
import com.busquedaCandidato.candidato.exception.type.ProcessNoExistException;
import com.busquedaCandidato.candidato.exception.type.StateNoFoundException;
import com.busquedaCandidato.candidato.exception.type.EntityNoExistException;
import com.busquedaCandidato.candidato.exception.type.ItAlreadyProcessWithIdPostulation;
import com.busquedaCandidato.candidato.exception.type.CannotApplyException;
import com.busquedaCandidato.candidato.exception.type.PostulationIsOffException;
import com.busquedaCandidato.candidato.exception.type.PhoneAlreadyExistException;
import com.busquedaCandidato.candidato.exception.type.RoleIdNoExistException;
import com.busquedaCandidato.candidato.exception.type.CandidateNoExistException;
import com.busquedaCandidato.candidato.exception.type.ProcessAlreadyExistException;
import com.busquedaCandidato.candidato.exception.type.CannotBeCreateCandidateProcessException;
import com.busquedaCandidato.candidato.exception.type.IdCardAlreadyExistException;
import com.busquedaCandidato.candidato.exception.type.BadRequestException;
import com.busquedaCandidato.candidato.exception.type.CandidateNoPostulationException;
import com.busquedaCandidato.candidato.exception.type.EntityAlreadyHasRelationException;
import com.busquedaCandidato.candidato.exception.type.ItAlreadyExistPostulationException;
import com.busquedaCandidato.candidato.exception.type.ResourceNotFoundException;
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

    @ExceptionHandler(IdCardAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> IdCardAlreadyExistException(IdCardAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.ID_CARD_ALREADY_EXISTS.getMessage()));
    }

    @ExceptionHandler(CannotBeCreateCandidateProcessException.class)
    public ResponseEntity<Map<String, String>> CannotBeCreateCandidateProcessException(CannotBeCreateCandidateProcessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANNOT_BE_CREATED_CANDIDATE_PROCESS.getMessage()));
    }

    @ExceptionHandler(EntityNoExistException.class)
    public ResponseEntity<Map<String, String>> EntityNoExistException(EntityNoExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.ENTITY_DOES_NOT_EXIST.getMessage()));
    }

    @ExceptionHandler(CandidateNoPostulationException.class)
    public ResponseEntity<Map<String, String>> CandidateNoPostulationException(CandidateNoPostulationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANDIDATE_DOES_NOT_POSTULATION.getMessage()));
    }

    @ExceptionHandler(ProcessAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> ProcessAlreadyExistException(ProcessAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PROCESS_ALREADY_EXIST.getMessage()));
    }

    @ExceptionHandler(CandidateNoExistException.class)
    public ResponseEntity<Map<String, String>> CandidateNoExistException(CandidateNoExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANDIDATE_DOES_NOT_EXIST.getMessage()));
    }

    @ExceptionHandler(RoleIdNoExistException.class)
    public ResponseEntity<Map<String, String>> RoleIdNoExistException(RoleIdNoExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.ROLE_ID_DOES_NOT_EXIST.getMessage()));
    }

    @ExceptionHandler(PhoneAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> PhoneAlreadyExistException(PhoneAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.PHONE_ALREADY_EXISTS.getMessage()));
    }

    @ExceptionHandler(PostulationIsOffException.class)
    public ResponseEntity<Map<String, String>> PostulationIsOffException(PostulationIsOffException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.POSTULATION_IS_OFF.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Error interno del servidor: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityAlreadyHasRelationException.class)
    public ResponseEntity<Map<String, String>> EntityAlreadyHasRelationException(EntityAlreadyHasRelationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.IT_HAS_RELATION.getMessage()));
    }

    @ExceptionHandler(CannotApplyException.class)
    public ResponseEntity<Map<String, String>> CannotApplyException(CannotApplyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANNOT_APPLY.getMessage()));
    }

    @ExceptionHandler(ItAlreadyExistPostulationException.class)
    public ResponseEntity<Map<String, String>> ItAlreadyExistException(ItAlreadyExistPostulationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.IT_ALREADY_EXIST_POSTULATION.getMessage()));
    }

    @ExceptionHandler(ItAlreadyProcessWithIdPostulation.class)
    public ResponseEntity<Map<String, String>> ItAlreadyProcessWithIdPostulation(ItAlreadyProcessWithIdPostulation ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.IT_ALREADY_PROCESS_POSTULATION.getMessage()));
    }
}

