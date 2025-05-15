package com.candidateSearch.searching.configuration;

import com.candidateSearch.searching.entity.StateEntity;
import com.candidateSearch.searching.enums.StateEnum;
import com.candidateSearch.searching.event.StatesInitializedEvent;
import com.candidateSearch.searching.repository.IStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    private final IStateRepository stateRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Bean
    public CommandLineRunner initStates() {

        return args -> {
            log.info("Starting state data initialization...");

            boolean success = false;

            try {
                long stateCount = stateRepository.count();

                if (stateCount == 0) {
                    log.info("No states found. Initializing state data...");

                    List<StateEntity> states = Arrays.stream(StateEnum.values())
                            .map(stateEnum -> {
                                StateEntity stateEntity = new StateEntity();
                                stateEntity.setId(stateEnum.getId());
                                stateEntity.setName(stateEnum.getStateName());
                                return stateEntity;
                            })
                            .toList();

                    stateRepository.saveAll(states);
                    log.info("State data initialized successfully. {} states created.", states.size());
                    success = true;
                } else {
                    log.info("States table already populated. Verifying consistency with enum...");

                    boolean consistent = true;
                    for (StateEnum stateEnum : StateEnum.values()) {
                        Optional<StateEntity> stateEntity = stateRepository.findById(stateEnum.getId());
                        if (stateEntity.isEmpty() || !stateEntity.get().getName().equals(stateEnum.getStateName())) {
                            log.warn("Inconsistency found: Enum state '{}' with id {} doesn't match database entry.",
                                    stateEnum.getStateName(), stateEnum.getId());
                            consistent = false;
                        }
                    }

                    if (consistent) {
                        log.info("Enum states and database states are consistent.");
                        success = true;
                    } else {
                        log.warn("There are inconsistencies between enum states and database states.");
                    }
                }
            } catch (Exception e) {
                log.error("Error during state initialization", e);
            } finally {
                eventPublisher.publishEvent(new StatesInitializedEvent(this, success));
            }
        };
    }
}
