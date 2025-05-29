package com.candidateSearch.searching.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CustomError {
    private String  code;
    private String message;
    private Map<String, List<String>> details;
    private LocalDate localDate;
}
