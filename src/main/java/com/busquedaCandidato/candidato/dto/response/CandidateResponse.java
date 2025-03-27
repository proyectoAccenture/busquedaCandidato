package com.busquedaCandidato.candidato.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CandidateResponse {
    private List<CandidateResponseDto> candidates;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}
