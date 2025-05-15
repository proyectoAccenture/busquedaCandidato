package com.candidateSearch.searching.service;

import com.candidateSearch.searching.enums.StateEnum;
import com.candidateSearch.searching.exception.type.InvalidStateTransitionException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StateTransitionManager {
    private final Map<StateEnum, List<StateEnum>> transitions = new EnumMap<>(StateEnum.class);

    @PostConstruct
    public void initTransitions() {

        transitions.put(StateEnum.NUEVO, List.of(StateEnum.VALIDACION_CCI));

        transitions.put(StateEnum.VALIDACION_CCI, List.of(StateEnum.EN_PRUEBA_TECNICA));

        transitions.put(StateEnum.EN_PRUEBA_TECNICA, List.of(StateEnum.EXPIRO_PRUEBA, StateEnum.TERMINO_PRUEBA));

        transitions.put(StateEnum.EXPIRO_PRUEBA, Collections.emptyList());

        transitions.put(StateEnum.TERMINO_PRUEBA, List.of(
                StateEnum.DESCARTADO_EN_PRUEBA_TECNICA,
                StateEnum.ENTREVISTA_LT_ACCENTURE));

        transitions.put(StateEnum.DESCARTADO_EN_PRUEBA_TECNICA, Collections.emptyList());

        transitions.put(StateEnum.ENTREVISTA_LT_ACCENTURE, List.of(
                StateEnum.DESCARTADO_LT_ACCENTURE,
                StateEnum.ENTREVISTA_CLIENTE));

        transitions.put(StateEnum.DESCARTADO_LT_ACCENTURE, Collections.emptyList());

        transitions.put(StateEnum.ENTREVISTA_CLIENTE, List.of(
                StateEnum.DESCARTADO_POR_CLIENTE,
                StateEnum.VALIDACION_BGC));

        transitions.put(StateEnum.DESCARTADO_POR_CLIENTE, Collections.emptyList());

        transitions.put(StateEnum.VALIDACION_BGC, List.of(
                StateEnum.DESCARTADO_EN_BGC,
                StateEnum.OFERTA_SALARIAL));

        transitions.put(StateEnum.DESCARTADO_EN_BGC, Collections.emptyList());

        transitions.put(StateEnum.OFERTA_SALARIAL, List.of(
                StateEnum.DECLINA,
                StateEnum.INGRESO));

        transitions.put(StateEnum.DECLINA, Collections.emptyList());

        transitions.put(StateEnum.INGRESO, Collections.emptyList());
    }

    public boolean isValidTransition(Long fromStateId, Long toStateId) {
        if (fromStateId.equals(toStateId)) {
            return true;
        }

        StateEnum fromState = StateEnum.getById(fromStateId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown source state ID: " + fromStateId));
        StateEnum toState = StateEnum.getById(toStateId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown target state ID: " + toStateId));

        return transitions.getOrDefault(fromState, Collections.emptyList())
                .contains(toState);
    }

    public void validateTransition(Long fromStateId, Long toStateId) {
        if (!isValidTransition(fromStateId, toStateId)) {
            StateEnum fromState = StateEnum.getById(fromStateId)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown source state ID: " + fromStateId));
            StateEnum toState = StateEnum.getById(toStateId)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown target state ID: " + toStateId));

            throw new InvalidStateTransitionException(
                    String.format("Invalid state transition from '%s' to '%s'",
                            fromState.getStateName(), toState.getStateName()));
        }
    }

    public List<Long> getNextValidStateIds(Long stateId) {
        StateEnum currentState = StateEnum.getById(stateId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state ID: " + stateId));

        return transitions.getOrDefault(currentState, Collections.emptyList())
                .stream()
                .map(StateEnum::getId)
                .collect(Collectors.toList());
    }
}
