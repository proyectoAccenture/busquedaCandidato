package com.candidateSearch.searching.exception.advice;

import com.candidateSearch.searching.error.CustomError;
import com.candidateSearch.searching.exception.type.CustomConflictException;
import com.candidateSearch.searching.exception.type.CustomBadRequestException;
import com.candidateSearch.searching.exception.type.CustomNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.candidateSearch.searching.exception.response.ErrorCatalog.DATABASE_ERROR;
import static com.candidateSearch.searching.exception.response.ErrorCatalog.INVALID_PARAMETERS;
import static com.candidateSearch.searching.exception.response.ErrorCatalog.NOT_FOUND;
import static com.candidateSearch.searching.exception.response.ErrorCatalog.GENERIC_ERROR;
import static com.candidateSearch.searching.exception.response.ErrorCatalog.READY_EXIST;
import static com.candidateSearch.searching.exception.response.ErrorCatalog.INFO_BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomNotFoundException.class)
    public CustomError handleCustomNotFoundException(CustomNotFoundException e){
        Map<String, List<String>> details = Map.of(
                "error", List.of(e.getMessage())
        );
        return CustomError.builder()
                .code(NOT_FOUND.getCode()+" ("+HttpStatus.NOT_FOUND+")")
                .message(NOT_FOUND.getMessage())
                .details(details)
                .localDate(LocalDate.now())
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomConflictException.class)
    public CustomError handleCustomAlreadyExistsException(CustomConflictException e){
        Map<String, List<String>> details = Map.of(
                "error", List.of(e.getMessage())
        );
        return CustomError.builder()
                .code(READY_EXIST.getCode()+" ("+HttpStatus.CONFLICT+")")
                .message(READY_EXIST.getMessage())
                .details(details)
                .localDate(LocalDate.now())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomBadRequestException.class)
    public CustomError handleCustomCreateException(CustomBadRequestException e){
        Map<String, List<String>> details = Map.of(
                "error", List.of(e.getMessage())
        );
        return CustomError.builder()
                .code(INFO_BAD_REQUEST.getCode()+" ("+HttpStatus.BAD_REQUEST+")")
                .message(INVALID_PARAMETERS.getMessage())
                .details(details)
                .localDate(LocalDate.now())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CustomError handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();

        Map<String, List<String>> fieldErrors = result.getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                DefaultMessageSourceResolvable::getDefaultMessage,
                                Collectors.toList()
                        )
                ));

        return CustomError.builder()
                .code(INVALID_PARAMETERS.getCode()+" ("+HttpStatus.BAD_REQUEST+")")
                .message(INVALID_PARAMETERS.getMessage())
                .details(fieldErrors)
                .localDate(LocalDate.now())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public CustomError handleInvalidFormatException(InvalidFormatException e){
        String field = e.getPath().get(0).getFieldName();
        Map<String, List<String>> details = Map.of(
                field, List.of("Invalid format for field '" + field + "'")
        );
        return CustomError.builder()
                .code(INVALID_PARAMETERS.getCode()+" ("+HttpStatus.BAD_REQUEST+")")
                .message(INVALID_PARAMETERS.getMessage())
                .details(details)
                .localDate(LocalDate.now())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CustomError handleGenericException(Exception e){
        Map<String, List<String>> details = Map.of(
                "error", List.of(e.getMessage())
        );
        return CustomError.builder()
                .code(GENERIC_ERROR.getCode()+" ("+HttpStatus.INTERNAL_SERVER_ERROR+")")
                .message(GENERIC_ERROR.getMessage())
                .details(details)
                .localDate(LocalDate.now())
                .build();
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler({CannotGetJdbcConnectionException.class, DataAccessException.class})
    public CustomError handleDatabaseConnectionException(Exception e) {
        Map<String, List<String>> details = Map.of(
                "database", List.of("Error connecting to database: " + e.getMessage())
        );
        return CustomError.builder()
                .code(DATABASE_ERROR.getCode()+" ("+HttpStatus.SERVICE_UNAVAILABLE+")")
                .message(DATABASE_ERROR.getMessage())
                .details(details)
                .localDate(LocalDate.now())
                .build();
    }


}
