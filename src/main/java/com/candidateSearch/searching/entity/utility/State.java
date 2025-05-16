package com.candidateSearch.searching.entity.utility;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public enum State {

    NUEVO(1L, "Nuevo"),
    VALIDACION_CCI(2L, "Validación CCI"),
    EN_PRUEBA_TECNICA(3L, "En prueba técnica"),
    EXPIRO_PRUEBA(4L, "Expiro prueba"),
    TERMINO_PRUEBA(5L, "Termino prueba"),
    DESCARTADO_EN_PRUEBA_TECNICA(6L, "Descartado en prueba técnica"),
    ENTREVISTA_LT_ACCENTURE(7L, "Entrevista LT Accenture"),
    DESCARTADO_LT_ACCENTURE(8L, "Descartado LT Accenture"),
    ENTREVISTA_CLIENTE(9L, "Entrevista Cliente"),
    DESCARTADO_POR_CLIENTE(10L, "Descartado por cliente"),
    VALIDACION_BGC(11L, "Validación BGC"),
    DESCARTADO_EN_BGC(12L, "Descartado en BGC"),
    OFERTA_SALARIAL(13L, "Oferta salarial"),
    DECLINA(14L, "Declina"),
    INGRESO(15L, "Ingreso");

    private final Long id;
    private final String stateName;

    private static final Map<Long, State> ID_MAP = new HashMap<>();

    static {
        for (State state : values()) {
            ID_MAP.put(state.getId(), state);
        }
    }

    State(Long id, String stateName) {
        this.id = id;
        this.stateName = stateName;
    }

    public static Optional<State> getById(Long id) {
        return Optional.ofNullable(ID_MAP.get(id));
    }
}
