package com.candidateSearch.searching.configuration.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StatesInitializedEvent extends ApplicationEvent {
    private final boolean success;

    public StatesInitializedEvent(Object source, boolean success) {
        super(source);
        this.success = success;
    }
}
