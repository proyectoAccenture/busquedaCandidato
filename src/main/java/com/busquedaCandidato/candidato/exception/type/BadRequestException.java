package com.busquedaCandidato.candidato.exception.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
