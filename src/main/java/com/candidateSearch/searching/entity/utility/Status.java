package com.candidateSearch.searching.entity.utility;

import lombok.Getter;

@Getter
public enum Status {
    INACTIVE(0),
    ACTIVE(1),
    BLOCKED(2);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public static Status fromValue(int value) {
        for (Status s : values()) {
            if (s.value == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }
}
