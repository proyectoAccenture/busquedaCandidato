package com.candidateSearch.searching.service.util;

import com.candidateSearch.searching.entity.StateEntity;
import com.candidateSearch.searching.enums.StateEnum;
import com.candidateSearch.searching.event.StatesInitializedEvent;
import com.candidateSearch.searching.repository.IStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j
public class StateIdResolver {

    private final IStateRepository stateRepository;
    private final Map<StateEnum, Long> resolvedIds = new EnumMap<>(StateEnum.class);
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @EventListener
    public void onStatesInitialized(StatesInitializedEvent event) {
        if (event.isSuccess()) {
            initialize();
        } else {
            log.warn("States initialization was not successful. StateIdResolver will not initialize now.");
        }
    }

    private synchronized void initialize() {
        if (initialized.get()) {
            return;
        }

        log.info("Initializing state ID resolver...");

        for (StateEnum state : StateEnum.values()) {
            Optional<StateEntity> entityById = stateRepository.findById(state.getId());

            if (entityById.isPresent() && entityById.get().getName().equals(state.getStateName())) {
                resolvedIds.put(state, state.getId());
            } else {
                Optional<StateEntity> entityByName = stateRepository.findByName(state.getStateName());
                if (entityByName.isPresent()) {
                    resolvedIds.put(state, entityByName.get().getId());
                    if (!entityByName.get().getId().equals(state.getId())) {
                        log.warn("ID mismatch for state {}: Enum has {} but DB has {}",
                                state.getStateName(), state.getId(), entityByName.get().getId());
                    }
                } else {
                    log.error("State {} not found in database", state.getStateName());
                }
            }
        }

        log.info("State ID resolver initialized with {} mapped states", resolvedIds.size());
        initialized.set(true);
    }

    private void ensureInitialized() {
        if (!initialized.get()) {
            initialize();
        }
    }

    public Long getIdForState(StateEnum stateEnum) {
        ensureInitialized();
        return resolvedIds.getOrDefault(stateEnum, stateEnum.getId());
    }

    public Optional<StateEnum> getStateForId(Long id) {
        ensureInitialized();
        return resolvedIds.entrySet().stream()
                .filter(entry -> entry.getValue().equals(id))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
