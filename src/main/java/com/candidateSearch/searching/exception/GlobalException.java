package com.candidateSearch.searching.exception;

import com.candidateSearch.searching.exception.response.ExceptionResponse;
import com.candidateSearch.searching.exception.type.BadRequestException;
import com.candidateSearch.searching.exception.type.CandidateBlockedException;
import com.candidateSearch.searching.exception.type.CandidateNoExistException;
import com.candidateSearch.searching.exception.type.CandidateNoPostulationException;
import com.candidateSearch.searching.exception.type.CannotApplyException;
import com.candidateSearch.searching.exception.type.CannotBeCreateException;
import com.candidateSearch.searching.exception.type.CannotBeUpdateException;
import com.candidateSearch.searching.exception.type.EntityAlreadyExistsException;
import com.candidateSearch.searching.exception.type.EntityAlreadyHasRelationException;
import com.candidateSearch.searching.exception.type.EntityNoExistException;
import com.candidateSearch.searching.exception.type.FieldAlreadyExistException;
import com.candidateSearch.searching.exception.type.InvalidFileTypeException;
import com.candidateSearch.searching.exception.type.InvalidStateTransitionException;
import com.candidateSearch.searching.exception.type.ItAlreadyExistPostulationException;
import com.candidateSearch.searching.exception.type.ItAlreadyProcessWithIdPostulation;
import com.candidateSearch.searching.exception.type.PostulationIsOffException;
import com.candidateSearch.searching.exception.type.ProcessAlreadyExistException;
import com.candidateSearch.searching.exception.type.ProcessClosedException;
import com.candidateSearch.searching.exception.type.ProcessNoExistException;
import com.candidateSearch.searching.exception.type.ResourceNotFoundException;
import com.candidateSearch.searching.exception.type.RoleIdNoExistException;
import com.candidateSearch.searching.exception.type.StateNoFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
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

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException ex) {
        String fieldName = ex.getPath().get(0).getFieldName();
        String message = "The value entered for the field '" + fieldName + "' is not valid";
        return ResponseEntity.badRequest().body(message);
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

    @ExceptionHandler(FieldAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleFieldAlreadyExistException(FieldAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", ex.getMessage()));
    }

    @ExceptionHandler(CannotBeCreateException.class)
    public ResponseEntity<Map<String, String>> CannotBeCreateCandidateProcessException(CannotBeCreateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANNOT_BE_CREATED.getMessage()));
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

    @ExceptionHandler(CannotBeUpdateException.class)
    public ResponseEntity<Map<String, String>> CannotBeUpdateException(CannotBeUpdateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANNOT_BE_UPDATE.getMessage()));
    }

    @ExceptionHandler(ItAlreadyProcessWithIdPostulation.class)
    public ResponseEntity<Map<String, String>> ItAlreadyProcessWithIdPostulation(ItAlreadyProcessWithIdPostulation ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.IT_ALREADY_PROCESS_POSTULATION.getMessage()));
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFileType(InvalidFileTypeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<Object> handleInvalidStateTransition(
            InvalidStateTransitionException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("error", "Invalid state transition");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CandidateBlockedException.class)
    public ResponseEntity<Map<String, String>> CandidateBlockedException(CandidateBlockedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CANDIDATE_BLOCKED.getMessage()));
    }
}

