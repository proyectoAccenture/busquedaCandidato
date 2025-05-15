package com.candidateSearch.searching.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "StateResponse", description = "Model represent a state on database")
@Data
public class StateResponseDto {

    @Schema(name = "id", defaultValue = "1", description = "Unique Id of state on database")
    private Long id;

    @Schema(name = "name", description = "State name")
    private String name;

}
