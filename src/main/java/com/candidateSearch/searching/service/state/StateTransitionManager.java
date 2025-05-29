package com.candidateSearch.searching.service.state;

import com.candidateSearch.searching.entity.utility.State;
import com.candidateSearch.searching.exception.type.CustomBadRequestException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StateTransitionManager {
    private final Map<State, List<State>> transitions = new EnumMap<>(State.class);

    @PostConstruct
    public void initTransitions() {

        transitions.put(State.NUEVO, List.of(State.VALIDACION_CCI));

        transitions.put(State.VALIDACION_CCI, List.of(State.EN_PRUEBA_TECNICA));

        transitions.put(State.EN_PRUEBA_TECNICA, List.of(State.EXPIRO_PRUEBA, State.TERMINO_PRUEBA));

        transitions.put(State.EXPIRO_PRUEBA, Collections.emptyList());

        transitions.put(State.TERMINO_PRUEBA, List.of(
                State.DESCARTADO_EN_PRUEBA_TECNICA,
                State.ENTREVISTA_LT_ACCENTURE));

        transitions.put(State.DESCARTADO_EN_PRUEBA_TECNICA, Collections.emptyList());

        transitions.put(State.ENTREVISTA_LT_ACCENTURE, List.of(
                State.DESCARTADO_LT_ACCENTURE,
                State.ENTREVISTA_CLIENTE));

        transitions.put(State.DESCARTADO_LT_ACCENTURE, Collections.emptyList());

        transitions.put(State.ENTREVISTA_CLIENTE, List.of(
                State.DESCARTADO_POR_CLIENTE,
                State.VALIDACION_BGC));

        transitions.put(State.DESCARTADO_POR_CLIENTE, Collections.emptyList());

        transitions.put(State.VALIDACION_BGC, List.of(
                State.DESCARTADO_EN_BGC,
                State.OFERTA_SALARIAL));

        transitions.put(State.DESCARTADO_EN_BGC, Collections.emptyList());

        transitions.put(State.OFERTA_SALARIAL, List.of(
                State.DECLINA,
                State.INGRESO));

        transitions.put(State.DECLINA, Collections.emptyList());

        transitions.put(State.INGRESO, Collections.emptyList());
    }

    public boolean isValidTransition(Long fromStateId, Long toStateId) {
        if (fromStateId.equals(toStateId)) {
            return true;
        }

        State fromState = State.getById(fromStateId)
                .orElseThrow(() -> new CustomBadRequestException("Unknown source state ID: " + fromStateId));
        State toState = State.getById(toStateId)
                .orElseThrow(() -> new CustomBadRequestException("Unknown target state ID: " + toStateId));

        return transitions.getOrDefault(fromState, Collections.emptyList())
                .contains(toState);
    }

    public void validateTransition(Long fromStateId, Long toStateId) {
        if (!isValidTransition(fromStateId, toStateId)) {
            State fromState = State.getById(fromStateId)
                    .orElseThrow(() -> new CustomBadRequestException("Unknown source state ID: " + fromStateId));
            State toState = State.getById(toStateId)
                    .orElseThrow(() -> new CustomBadRequestException("Unknown target state ID: " + toStateId));

            throw new CustomBadRequestException(
                    String.format("Invalid state transition from '%s' to '%s'",
                            fromState.getStateName(), toState.getStateName()));
        }
    }

    public List<Long> getNextValidStateIds(Long stateId) {
        State currentState = State.getById(stateId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state ID: " + stateId));

        return transitions.getOrDefault(currentState, Collections.emptyList())
                .stream()
                .map(State::getId)
                .collect(Collectors.toList());
    }
}
