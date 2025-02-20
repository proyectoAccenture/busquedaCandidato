package com.busquedaCandidato.candidato.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddStateResponseDto {

    private final Long id;
    private final String state;
}
